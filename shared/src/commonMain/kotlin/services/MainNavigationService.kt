package services

expect class MainNavigationService {
    suspend fun getFirstLinks(): List<String>
}