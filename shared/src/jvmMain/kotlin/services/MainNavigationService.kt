package services

import database.GamesSections
import database.Teams
import database.WorkoutsSections
import database.database
import org.jetbrains.exposed.sql.selectAll
import rpc.RPCService

actual class MainNavigationService: RPCService {
    actual suspend fun getFirstLinks(): List<String> {
        val mainNavigationList = mutableListOf<String>()
        database{
            GamesSections.selectAll().first().let{
                mainNavigationList += it[GamesSections.link]
            }
        }
        database {
            Teams.selectAll().first().let{
                mainNavigationList += it[Teams.link]
            }
        }
        database {
            WorkoutsSections.selectAll().first().let{
                mainNavigationList += it[WorkoutsSections.link]
            }
        }
        database {
            GamesSections.selectAll().first().let{
                mainNavigationList += it[GamesSections.link]
            }
        }
        return mainNavigationList
    }
}