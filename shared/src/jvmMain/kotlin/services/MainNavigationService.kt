package services

import database.*
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
            GallerySections.selectAll().first().let{
                mainNavigationList += it[GallerySections.link]
            }
        }
        return mainNavigationList
    }
}