package services

import database.Teams
import database.database
import model.Team
import org.jetbrains.exposed.sql.selectAll
import rpc.RPCService

actual class TeamService : RPCService {
    actual suspend fun getTeam(name: String): Team {
        return database {
            Teams.selectAll().first().let {
                Team(it[Teams.teamId], it[Teams.teamName], it[Teams.trainerId])
            }
        }
    }

    actual suspend fun editTeam(team: Team) {
    }

    actual suspend fun addTeam(team: Team) {
    }

    actual suspend fun deleteTeam(team: Team) {
    }

}