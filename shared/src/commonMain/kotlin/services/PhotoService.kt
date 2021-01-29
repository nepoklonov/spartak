package services

import model.PhotoDTO

expect class PhotoService {
    suspend fun getAllPhotosBySection(section: String): List<String>
    suspend fun addPhoto(photo: PhotoDTO) : Int
    suspend fun deletePhoto(id: Int): Boolean
}