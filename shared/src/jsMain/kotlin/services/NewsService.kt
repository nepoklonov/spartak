package services

import Annotations.RequireRole
import kotlinx.serialization.builtins.serializer
import model.NewsDTO
import model.NewsTripleDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class NewsService (coroutineContext: CoroutineContext){
    private val transport = Transport(coroutineContext)

    actual suspend fun getNewsById(id: Int): NewsDTO{
        return transport.get("getNewsById", NewsDTO.serializer(), "id" to id)
    }

    actual suspend fun getLastNews(number: Int?): List<NewsDTO> {
        return transport.getList("getLastNews", NewsDTO.serializer(), "number" to number)
    }

    suspend fun getLastNews(): List<NewsDTO> = getLastNews(null)

    actual suspend fun deleteNews(id: Int): Boolean {
        transport.post("deleteNews", Boolean.serializer(), "id" to id)
        return true
    }

    actual suspend fun addNews(news: NewsDTO): Int {
        return transport.post("addNews", Int.serializer(), "news" to news)
    }
    actual suspend fun getNextNewId(): Int {
        return transport.post("getNextNewId", Int.serializer())
    }

    actual suspend fun getNewsTripleById(id: Int): NewsTripleDTO {
        return transport.get("getNewsTripleById", NewsTripleDTO.serializer(), "id" to id)
    }
}