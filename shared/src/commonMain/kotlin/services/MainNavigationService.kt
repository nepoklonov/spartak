package services

import model.NavigationDTO

expect class MainNavigationService {
    suspend fun getFirstLinks(): List<String>
}