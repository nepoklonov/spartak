package services

import database.GallerySections
import database.database
import model.NavigationDTO
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import rpc.RPCService

actual class GalleryNavigationService : RPCService {
    actual suspend fun getGalleryNavigationList(): List<NavigationDTO> {
        val navigationList = mutableListOf<NavigationDTO>()
        database {
            GallerySections.selectAll().forEach {
                navigationList += NavigationDTO(
                    it[GallerySections.id].value,
                    it[GallerySections.header],
                    it[GallerySections.link]
                )
            }
        }
        return navigationList
    }

    actual suspend fun addGallerySection(navigationDTO: NavigationDTO): Int {
        return database {
            GallerySections.insertAndGetId {
                it[header] = navigationDTO.header
                it[link] = navigationDTO.link
            }
        }.value
    }

    actual suspend fun editGallerySection(navigationDTO: NavigationDTO): Boolean {
        database {
            GallerySections.update({ GallerySections.id eq navigationDTO.id }) {
                it[header] = navigationDTO.header
                it[link] = navigationDTO.link
            }
        }
        return true
    }

    actual suspend fun deleteGallerySection(id: Int): Boolean {
        database {
            GallerySections.deleteWhere { GallerySections.id eq id }
        }
        return true
    }
}