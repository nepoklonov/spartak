package services

import kotlinx.serialization.builtins.serializer
import model.TeamDTO
import model.TeamMemberDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class TeamService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)

    actual suspend fun addTeam(team: TeamDTO): Int {
        return transport.post("addTeam", Int.serializer(), "team" to team)
    }

    actual suspend fun addTeamMember(newTeamMember: TeamMemberDTO): Int {
        return transport.post("addTeamMember", Int.serializer(), "newTeamMember" to newTeamMember)
    }

    actual suspend fun getTeamById(id: Int): TeamDTO {
        return transport.get("getTeamById", TeamDTO.serializer(), "id" to id)
    }

    actual suspend fun getAllTeamsByYear(year: String): List<TeamDTO> {
        return transport.getList("getAllTeamsByYear", TeamDTO.serializer(), "year" to year)
    }

    actual suspend fun getAllTeamMembers(teamId: Int): List<TeamMemberDTO> {
        return transport.getList("getAllTeamMembers", TeamMemberDTO.serializer(), "name" to teamId.toString())
    }

    actual suspend fun getTeamMemberByRole(role: String): List<TeamMemberDTO> {
        return transport.getList("getTeamMemberByRole", TeamMemberDTO.serializer(), "role" to role)
    }

    actual suspend fun editTeam(team: TeamDTO): Boolean {
        transport.post("editTeam", Boolean.serializer(), "team" to team)
        return true
    }

    actual suspend fun editTeamMember(teamMember: TeamMemberDTO): Boolean {
        return transport.post("editTeamMember", Boolean.serializer(), "teamMember" to teamMember)
    }

    actual suspend fun deleteTeam(teamId: Int): Boolean {
        transport.post("deleteTeam", Boolean.serializer(), "team" to teamId)
        return true
    }

    actual suspend fun deleteTeamMember(id: Int): Boolean {
        return transport.post("deleteTeamMember", Boolean.serializer(), "id" to id)
    }
}