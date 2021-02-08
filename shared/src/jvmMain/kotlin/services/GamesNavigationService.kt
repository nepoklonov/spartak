package services

import database.GamesSections
import database.database
import model.NavigationDTO
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import rpc.RPCService

actual class GamesNavigationService: RPCService {
    actual suspend fun getGamesNavigationList(): List<NavigationDTO> {
        val navigationList = mutableListOf<NavigationDTO>()
        database {
            GamesSections.selectAll().forEach {
                navigationList += NavigationDTO(
                    it[GamesSections.id].value,
                    it[GamesSections.header],
                    it[GamesSections.link]
                )
            }
        }
        return navigationList
    }

    actual suspend fun addGamesSection(navigationDTO: NavigationDTO): Int {
        return database {
            GamesSections.insertAndGetId {
                it[header] = navigationDTO.header
                it[link] = navigationDTO.link
            }
        }.value
    }

    actual suspend fun editGamesSection(navigationDTO: NavigationDTO): Boolean {
        database {
            GamesSections.update({ GamesSections.id eq navigationDTO.id }) {
                it[header] = navigationDTO.header
                it[link] = navigationDTO.link
            }
        }
        return true
    }

    actual suspend fun deleteGamesSection(id: Int): Boolean {
        database {
            GamesSections.deleteWhere { GamesSections.id eq id }
        }
        return true
    }
}