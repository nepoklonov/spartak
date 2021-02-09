package services

import model.NewsDTO

expect class NewsService {
    suspend fun getNewsById(id: Int) : NewsDTO
    suspend fun deleteNews(id: Int): Boolean
    suspend fun addNews(news: NewsDTO): Int
    suspend fun getLastNews(number: Int): List<NewsDTO>
}