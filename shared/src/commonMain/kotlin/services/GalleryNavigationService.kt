package services

import model.NavigationDTO

expect class GalleryNavigationService {
    suspend fun getGalleryNavigationList(): List<NavigationDTO>
    suspend fun addGallerySection(navigationDTO: NavigationDTO): Int
    suspend fun editGallerySection(navigationDTO: NavigationDTO): Boolean
    suspend fun deleteGallerySection(id: Int): Boolean

}