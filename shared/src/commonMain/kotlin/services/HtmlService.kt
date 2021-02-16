package services

expect class HtmlService {
    suspend fun editHtml(url: String, content: String): Boolean

}