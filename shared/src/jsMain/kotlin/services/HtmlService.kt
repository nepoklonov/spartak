package services

import kotlinx.serialization.builtins.serializer
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class HtmlService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)

    suspend fun getHtml(url: String): String{
        return transport.get(url, String.serializer(), isJson = false)
    }
    actual suspend fun editHtml(url: String, content: String): Boolean{
        return transport.post("editHtml", Boolean.serializer(), "url" to url, "content" to content)
    }
}