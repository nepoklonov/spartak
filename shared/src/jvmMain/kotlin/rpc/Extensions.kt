package rpc

import Annotations.RequireRole
import LoginSession
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure

// Json.decodeFromString(param.type.jvmErasure.serializer(), call.request.queryParameters[it].toString())
@OptIn(InternalSerializationApi::class)
@Suppress("UNCHECKED_CAST")

fun Route.rpc(serviceClass: KClass<out RPCService>, vararg blocks: Pair<KFunction<*>, (ApplicationCall, Any) -> Unit>) {
    val instance = serviceClass.createInstance()

    suspend fun queryBody(function: KFunction<*>, call: ApplicationCall, args: MutableList<Any?>) {

        // check rights
        function.findAnnotation<RequireRole>()?.let {
            val role = call.sessions.get<LoginSession>()?.role ?: Role.Basic
            if (it.role != role) {
                call.respond(HttpStatusCode.Forbidden)
                return
            }
        }

        val result = function.callSuspend(*args.toTypedArray())!!
        blocks.find { it.first.name == function.name }?.let {
            it.second(call, result)
        }
        val serializedResult = if (function.returnType.arguments.isNotEmpty()) {
            when {
                function.returnType.isSubtypeOf(List::class.createType(function.returnType.arguments)) -> Json.encodeToString(
                    ListSerializer(function.returnType.arguments.first().type?.jvmErasure!!.serializer() as KSerializer<Any>),
                    result as List<Any>
                )
                function.returnType.isSubtypeOf(Set::class.createType(function.returnType.arguments)) -> Json.encodeToString(
                    SetSerializer(function.returnType.arguments.first().type?.jvmErasure!!.serializer() as KSerializer<Any>),
                    result as Set<Any>
                )
                else -> SerializationException("Method must return either List<R> or Set<R>, but it returns ${function.returnType}")
            }
        } else {
            Json.encodeToString(result::class.serializer() as KSerializer<Any>, result)
        }
        call.respond(serializedResult)
    }

    serviceClass.declaredMemberFunctions.map { function ->
        if (function.name.startsWith("get")) {
            get(function.name) {
                val args = mutableListOf<Any?>(instance)
                function.valueParameters.mapTo(args) { param ->
                    call.request.queryParameters[param.name.toString()]
                        ?.takeIf { it != null.toString() }
                        ?.let { strValue ->
                            Json { isLenient = true }.decodeFromString(
                                param.type.jvmErasure.serializer(),
                                strValue
                            )
                        }
                }
                queryBody(function, call, args)
            }
        } else {
            post(function.name) {
                val queryParameters = Json { isLenient = true }.decodeFromString(
                    MapSerializer(String.serializer(), String.serializer()),
                    call.receiveText()
                )
                val args = mutableListOf<Any?>(instance)
                function.valueParameters.mapTo(args) { param ->
                    Json { isLenient = true }.decodeFromString(
                        param.type.jvmErasure.serializer(),
                        queryParameters[param.name!!] ?: error("param is missing")
                    )
                }
                queryBody(function, call, args)
            }
        }
    }
}