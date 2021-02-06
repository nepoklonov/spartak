package services

import kotlinx.serialization.builtins.serializer
import model.NewsDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class NewsService (coroutineContext: CoroutineContext){
    private val transport = Transport(coroutineContext)
    actual suspend fun getNewsById(id: Int): String{
        return transport.get("getNewsById", String.serializer(), "id" to id)
    }

    actual suspend fun deleteNews(id: Int): Boolean {
        transport.post("deleteNews", Boolean.serializer(), "id" to id)
        return true
    }

    actual suspend fun addNews(news: NewsDTO): Int {
        return transport.post("addNews", Int.serializer(), "news" to news)
    }
}