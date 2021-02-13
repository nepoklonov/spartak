package services

import Annotations.RequireRole
import database.Photos
import database.database
import model.PhotoDTO
import org.jetbrains.exposed.sql.select
import rpc.RPCService

actual class PhotoService: RPCService {
    actual suspend fun getAllPhotosBySection(section: String): List<String> {
        val listOfPhotosUrl = mutableListOf<String>()
        database {
            Photos.select { Photos.gallerySection eq section }.forEach {
                listOfPhotosUrl += it[Photos.url]
            }
        }
        return listOfPhotosUrl
    }

    @RequireRole(Role.Admin)
    actual suspend fun addPhoto(photo: PhotoDTO): Int {
        TODO("Not yet implemented")
    }

    @RequireRole(Role.Admin)
    actual suspend fun deletePhoto(id: Int): Boolean {
        TODO("Not yet implemented")
    }
}