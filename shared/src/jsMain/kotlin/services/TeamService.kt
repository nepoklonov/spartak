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

    actual suspend fun getTeam(name: String): TeamDTO {
        return transport.get("getTeam", TeamDTO.serializer(), "name" to name)
    }

    actual suspend fun getAllTeamsByYear(year: String): List<TeamDTO> {
        return transport.getList("getTeamsByYear", TeamDTO.serializer(), "year" to year)
    }

    actual suspend fun getAllTeamMembers(teamId: Int): List<TeamMemberDTO> {
        return transport.getList("getAllTeamMembers", TeamMemberDTO.serializer(), "name" to teamId)
    }

    actual suspend fun getTeamMemberById(id: Int): TeamMemberDTO {
        return transport.get("getTeamMemberById", TeamMemberDTO.serializer(), "id" to id)
    }

    actual suspend fun editTeam(team: TeamDTO): Boolean {
        transport.post("editTeam", Boolean.serializer(), "team" to team)
        return true
    }

    actual suspend fun editTeamMember(teamMember: String): Boolean {
        return transport.post("addTeamMember", Boolean.serializer(), "teamMember" to teamMember)
    }

    actual suspend fun deleteTeamById(teamId: Int): Boolean {
        transport.post("deleteTeam", Boolean.serializer(), "team" to teamId)
        return true
    }

    actual suspend fun deleteTeamMemberById(id: Int): Boolean {
        return transport.post("addTeamMember", Boolean.serializer(), "id" to id)
    }
}