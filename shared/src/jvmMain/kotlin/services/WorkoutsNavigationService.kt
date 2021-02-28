package services


import database.WorkoutsSections
import database.database
import model.NavigationDTO
import org.jetbrains.exposed.sql.*
import rpc.RPCService

actual class WorkoutsNavigationService : RPCService {
    actual suspend fun getWorkoutsNavigationList(): List<NavigationDTO> {
        val navigationList = mutableListOf<NavigationDTO>()
        database {
            WorkoutsSections.selectAll().orderBy(WorkoutsSections.id to SortOrder.ASC).forEach {
                navigationList += NavigationDTO(
                    it[WorkoutsSections.id].value,
                    it[WorkoutsSections.header],
                    it[WorkoutsSections.link]
                )
            }
        }
        return navigationList
    }

    actual suspend fun addWorkoutsSection(navigationDTO: NavigationDTO): Int {
        return database {
            WorkoutsSections.insertAndGetId {
                it[header] = navigationDTO.header
                it[link] = navigationDTO.link
            }
        }.value
    }

    actual suspend fun editWorkoutsSection(navigationDTO: NavigationDTO): Boolean {
        database {
            WorkoutsSections.update({ WorkoutsSections.id eq navigationDTO.id }) {
                it[header] = navigationDTO.header
                it[link] = navigationDTO.link
            }
        }
        return true
    }

    actual suspend fun deleteWorkoutsSection(id: Int): Boolean {
        database {
            WorkoutsSections.deleteWhere { WorkoutsSections.id eq id }
        }
        return true
    }
}