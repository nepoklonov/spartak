package rpc

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.*

//filter { it.name.startsWith("get") }

@Suppress("UNCHECKED_CAST")
fun <T : RPCService, R> Route.rpc(serviceClass: KClass<T>, serializer: KSerializer<R>) {
    val instance = serviceClass.createInstance()
    
    suspend fun queryBody(function: KFunction<*>, call: ApplicationCall) {
        val args = mutableListOf<Any>()
        args.add(instance)
        function.valueParameters.mapNotNullTo(args) { param ->
            println(param.name)
            param.name?.let {
                println(call.request.queryParameters)
                call.request.queryParameters[it] }
        }

        val result = function.callSuspend(*args.toTypedArray())
        val serializedResult = if (function.returnType.arguments.isNotEmpty()) {
            when {
                function.returnType.isSubtypeOf(List::class.createType(function.returnType.arguments)) -> Json.encodeToString(
                        ListSerializer(serializer),
                        result as List<R>
                )
                function.returnType.isSubtypeOf(Set::class.createType(function.returnType.arguments)) -> Json.encodeToString(
                        SetSerializer(serializer),
                        result as Set<R>
                )
                else -> SerializationException("Method must return either List<R> or Set<R>, but it returns ${function.returnType}")
            }
        } else {
            Json.encodeToString(serializer, result as R)
        }
        call.respond(serializedResult)
    }
    
    serviceClass.declaredMemberFunctions.map { function ->
        if (function.name.startsWith("get")) {
            get(function.name) {
                queryBody(function, call)
            }
        }else{
            post(function.name) {
                queryBody(function, call)
            }
            
        }
        
    }
}