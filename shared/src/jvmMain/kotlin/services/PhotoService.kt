package services

import Annotations.RequireRole
import database.Photos
import database.database
import model.PhotoDTO
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import rpc.RPCService

actual class PhotoService: RPCService {
    actual suspend fun getAllPhotosBySection(section: String): List<PhotoDTO> {
        val listOfPhotosUrl = mutableListOf<PhotoDTO>()
        database {
            Photos.select { Photos.gallerySection eq section }.forEach {
                listOfPhotosUrl += PhotoDTO(
                    it[Photos.id].value,
                    it[Photos.url],
                    it[Photos.gallerySection]
                )
            }
        }
        return listOfPhotosUrl
    }

    @RequireRole(Role.Admin)
    actual suspend fun addPhoto(photo: PhotoDTO): Int {
        return database{
            Photos.insertAndGetId {
                it[url] = photo.url
                it[gallerySection] = photo.gallerySection
            }
        }.value
    }

    @RequireRole(Role.Admin)
    actual suspend fun deletePhoto(id: Int): Boolean {
        database{
            Photos.deleteWhere { Photos.id eq id }
        }
        return true
    }
}