package services

import database.*
import database.News.url
import model.GameDTO
import model.NewsDTO
import model.TeamDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import rpc.RPCService


actual class NewsService : RPCService {
    private fun News.getNewsDTO(it: ResultRow): NewsDTO {
        return NewsDTO(
                it[News.id].value,
                it[url]
        )
    }
    private fun News.insertNewsToDatabase(it: UpdateBuilder<Int>, news: NewsDTO){
        it[url] = news.url
    }
    actual suspend fun getNewsById(id: Int): NewsDTO {
        return database {
          News.select { News.id eq id }.first().let{
              News.getNewsDTO(it)
          }
        }
    }
    actual suspend fun getLastNews(number: Int): List<NewsDTO> {
        return database {
            News.selectAll().orderBy(News.id, SortOrder.DESC).limit(number).toList().map{
            News.getNewsDTO(it)
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