package services

import kotlinx.serialization.builtins.serializer
import rpc.Transport
import kotlin.coroutines.CoroutineContext

class HtmlService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)

    suspend fun getHtml(url: String): String{
        return transport.get(url, String.serializer(), isJson = false)
    }
    suspend fun editHtml(url: String): Boolean{
        return transport.post(url, Boolean.serializer(), isJson = false)
    }
}