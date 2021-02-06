package services

import database.Photos
import database.database
import model.PhotoDTO
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
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

    actual suspend fun addPhoto(photo: PhotoDTO): Int {
        return database{
            Photos.insertAndGetId {
                it[url] = photo.url
                it[gallerySection] = photo.gallerySection
            }
        }.value
    }

    actual suspend fun deletePhoto(id: Int): Boolean {
        database{
            Photos.deleteWhere { Photos.id eq id }
        }
        return true
    }
}