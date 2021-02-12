package services

import database.*
import database.News.url
import model.GameDTO
import model.NewsDTO
import model.NewsTripleDTO
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
//    private fun News.getNewsTripleDTO(it: ResultRow): NewsTripleDTO {
//        return NewsTripleDTO(
//                it[News.id].value,
//                it[url],
//                it[prevId],
//                it[nextId]
//        )
//    }
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
    actual suspend fun getNewsTripleById(id: Int): NewsTripleDTO {
        var news = NewsDTO(0, "")
        database {
            News.select { News.id eq id }.first().let{
                news = News.getNewsDTO(it)
            }
        }
        var next = database {
            News.select {News.id greater id}.orderBy(News.id).first().let { it[News.id].value }
        }
        var prev = database {
            News.select {News.id less id}.orderBy(News.id, SortOrder.DESC).first().let { it[News.id].value }
        }
        return NewsTripleDTO(news.id, news.url, prev, next)
    }
    actual suspend fun getLastNews(number: Int): List<NewsDTO> {
        return database {
            News.selectAll().orderBy(News.id, SortOrder.DESC).limit(number).toList().map{
            News.getNewsDTO(it)
            }
        }
    }
    actual suspend fun getLastNews(): List<NewsDTO> {
        return database {
            News.selectAll().orderBy(News.id, SortOrder.DESC).toList().map{
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