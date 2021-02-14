package services

import kotlinx.serialization.builtins.serializer
import rpc.Transport
import kotlin.coroutines.CoroutineContext


actual class MainNavigationService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)
    actual suspend fun getFirstLinks(): List<String> {
        return transport.getList("getFirstLinks", String.serializer())
    }
}