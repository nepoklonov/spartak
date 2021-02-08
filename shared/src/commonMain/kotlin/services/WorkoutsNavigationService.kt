package services

import model.NavigationDTO

expect class WorkoutsNavigationService {
    suspend fun getWorkoutsNavigationList(): List<NavigationDTO>
    suspend fun addWorkoutsSection(navigationDTO: NavigationDTO): Int
    suspend fun editWorkoutsSection(navigationDTO: NavigationDTO): Boolean
    suspend fun deleteWorkoutsSection(id: Int): Boolean
}