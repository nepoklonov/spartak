package services

import database.GameCalendar
import database.News
import database.News.url
import database.Teams
import database.database
import model.GameDTO
import model.NewsDTO
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import rpc.RPCService

actual class NewsService : RPCService {
    private fun News.insertNewsToDatabase(it: UpdateBuilder<Int>, news: NewsDTO){
        it[url] = news.url
    }
    actual suspend fun getNewsById(id: Int): String {
        return database {
          News.select { News.id eq id }.first().let{
              it[url]
          }
        }
    }

    actual suspend fun deleteNews(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    actual suspend fun addNews(news: NewsDTO): Int {
        return database {
            News.insertAndGetId{
                insertNewsToDatabase(it, news)
            }
        }.value
    }
}