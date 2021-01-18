package services

import kotlinx.serialization.builtins.serializer
import model.TeamMemberDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class TeamMembersService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)
    actual suspend fun getTeamMemberById(id: String): TeamMemberDTO {
        return transport.get("getTeamMemberById", TeamMemberDTO.serializer(), "id" to id)
    }

    actual suspend fun addTeamMember(newTeamMember: String): Int {
        return transport.post("addTeamMember", Int.serializer(), "newTeamMember" to newTeamMember)
    }

    actual suspend fun editTeamMember(teamMember: String): Boolean {
        return transport.post("addTeamMember", Boolean.serializer(), "teamMember" to teamMember)
    }

    actual suspend fun deleteTeamMemberById(id: String): Boolean {
        return transport.post("addTeamMember", Boolean.serializer(), "id" to id)
    }
}