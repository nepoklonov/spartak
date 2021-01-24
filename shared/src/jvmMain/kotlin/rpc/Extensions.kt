package rpc

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
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

//                Json.decodeFromString(param.type.jvmErasure.serializer(), call.request.queryParameters[it].toString())
@OptIn(InternalSerializationApi::class)
@Suppress("UNCHECKED_CAST")

fun <T : RPCService> Route.rpc(serviceClass: KClass<T>) {
    val instance = serviceClass.createInstance()

    suspend fun queryBody(function: KFunction<*>, call: ApplicationCall, args: MutableList<Any>) {
        val result = function.callSuspend(*args.toTypedArray())!!
        val serializedResult = if (function.returnType.arguments.isNotEmpty()) {
            when {
                function.returnType.isSubtypeOf(List::class.createType(function.returnType.arguments)) -> Json.encodeToString(
                        ListSerializer(result::class.serializer() as KSerializer<Any>),
                        result as List<T>
                )
                function.returnType.isSubtypeOf(Set::class.createType(function.returnType.arguments)) -> Json.encodeToString(
                        SetSerializer(result::class.serializer() as KSerializer<Any>),
                        result as Set<T>
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
                val args = mutableListOf<Any>(instance)
                function.valueParameters.mapTo(args) { param ->
                    Json.decodeFromString(
                            param.type.jvmErasure.serializer(),
                            call.request.queryParameters[param.name.toString()].toString()
                    )
                }
                queryBody(function, call, args)
            }
        } else {
            post(function.name) {
                val args = mutableListOf<Any>(instance)
                function.valueParameters.mapTo(args) { param ->
                    Json.decodeFromString(
                            param.type.jvmErasure.serializer(),
                            Json{isLenient = true}.decodeFromString(
                                    MapSerializer(String.serializer(), String.serializer()),
                                    call.receiveText()
                            ).values.first()
                    )
                }
                queryBody(function, call, args)
            }
        }
    }
}