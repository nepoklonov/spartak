package services

import model.TeamDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class TeamService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)
    actual suspend fun getTeam(name: String): TeamDTO {
        TODO("Not yet implemented")
    }
    actual suspend fun getTeamByYear(year: Int): List<TeamDTO> = TODO()
    actual suspend fun editTeam(team: TeamDTO): Boolean = TODO()
    actual suspend fun addTeam(team: TeamDTO): Int = TODO()
    actual suspend fun deleteTeam(team: TeamDTO): Boolean = TODO()
}