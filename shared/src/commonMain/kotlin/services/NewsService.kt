package services

import model.NewsDTO

expect class NewsService {
    suspend fun getNewsById(id: Int) : String
    suspend fun deleteNews(id: Int): Boolean
    suspend fun addNews(news: NewsDTO): Int
}