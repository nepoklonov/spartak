package services

import model.NavigationDTO

expect class GamesNavigationService {
    suspend fun getGamesNavigationList(): List<NavigationDTO>
    suspend fun addGamesSection(navigationDTO: NavigationDTO): Int
    suspend fun editGamesSection(navigationDTO: NavigationDTO): Boolean
    suspend fun deleteGamesSection(id: Int): Boolean
}