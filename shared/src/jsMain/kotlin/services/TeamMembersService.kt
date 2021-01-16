package services

import model.TeamMemberDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class TeamMembersService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)
    actual suspend fun getTeamMemberById(id: String): TeamMemberDTO {
        return transport.get("getTeamMemberById", TeamMemberDTO.serializer(), "id" to id)
    }

    actual suspend fun addTeamMember(newTeamMember: TeamMemberDTO): Int = TODO()
    actual suspend fun editTeamMember(teamMember: TeamMemberDTO): Boolean = TODO()
    actual suspend fun deleteTeamMemberById(id: Int): Boolean = TODO()
}