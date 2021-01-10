package services

import model.Team
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class TeamService (coroutineContext: CoroutineContext){
    private val transport = Transport(coroutineContext)
    actual suspend fun getTeam(name: String): Team {
        TODO("Not yet implemented")
    }

    actual suspend fun editTeam(team: Team) {
    }

    actual suspend fun addTeam(team: Team) {
    }

    actual suspend fun deleteTeam(team: Team) {
    }

}