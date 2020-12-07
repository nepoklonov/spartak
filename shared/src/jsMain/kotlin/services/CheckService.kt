package services

import model.Check
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class CheckService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)

    actual suspend fun getCheck(): Check {
        return transport.get("getCheck", Check.serializer())
    }
}