package services

import Annotations.RequireRole
import database.*
import model.NewsDTO
import model.NewsTripleDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import rpc.RPCService


actual class NewsService : RPCService {
    private fun News.getNewsDTO(it: ResultRow): NewsDTO {
        return NewsDTO(
            it[News.id].value,
            it[url],
            it[date]
        )
    }


    private fun News.insertNewsToDatabase(it: UpdateBuilder<Int>, news: NewsDTO) {
        it[url] = news.url
        it[date] = news.date
    }

    actual suspend fun getNewsById(id: Int): NewsDTO {
        return database {
            News.select { News.id eq id }.first().let {
                News.getNewsDTO(it)
            }
        }
    }
    actual suspend fun getNextNewId(): Int {
        return database {
            News.selectAll().orderBy(News.id, SortOrder.DESC).first().let {
                it[News.id].value + 1
            }
        }
    }

    actual suspend fun getNewsTripleById(id: Int): NewsTripleDTO {
        var news = NewsDTO(0, "", 0L)
        var next :Int? = null
        var prev :Int? = null
        database {
            News.select { News.id eq id }.first().let {
                news = News.getNewsDTO(it)
            }
            if (id!= News.selectAll().orderBy(News.id, SortOrder.DESC).first().let { it[News.id].value })
                next = News.select { News.id greater id}.orderBy(News.id).first().let { it[News.id].value }
            if (id!= 1) prev = News.select { News.id less id}.orderBy(News.id, SortOrder.DESC).first().let { it[News.id].value }
        }
        return NewsTripleDTO(news.id, news.url, news.date, prev, next)
    }

    actual suspend fun getLastNews(number: Int?): List<NewsDTO> {
        return database {
            News.selectAll().orderBy(News.id, SortOrder.DESC).run {
                if (number != null) limit(number) else this
            }.map {
                News.getNewsDTO(it)
            }
        }
    }

    @RequireRole(Role.Admin)
    actual suspend fun deleteNews(id: Int): Boolean {
        database { News.deleteWhere { News.id eq id }
        }
        return true
    }

    @RequireRole(Role.Admin)
    actual suspend fun addNews(news: NewsDTO): Int {
        return database {
            News.insertAndGetId {
                insertNewsToDatabase(it, news)
            }
        }.value
    }

}