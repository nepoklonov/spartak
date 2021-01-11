package services

import database.Teams
import database.database
import model.TeamDTO
import org.jetbrains.exposed.sql.selectAll
import rpc.RPCService

actual class TeamService : RPCService {
    actual suspend fun getTeam(name: String): TeamDTO {
        return database {
            Teams.selectAll().first().let {
                TeamDTO(it[Teams.id].value,
                        it[Teams.name],
                        it[Teams.isOur],
                        it[Teams.type],
                        it[Teams.trainerId])
            }
        }
    }
    actual suspend fun getTeamByYear(year: Int): List<TeamDTO> = TODO()
    actual suspend fun editTeam(team: TeamDTO): Boolean = TODO()
    actual suspend fun addTeam(team: TeamDTO): Int = TODO()
    actual suspend fun deleteTeam(team: TeamDTO): Boolean = TODO()
}