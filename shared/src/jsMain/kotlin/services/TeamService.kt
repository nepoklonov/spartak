package services

import Annotations.RequireRole
import kotlinx.serialization.builtins.serializer
import model.TeamDTO
import model.TeamMemberDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class TeamService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)

    @RequireRole(Role.Admin)
    actual suspend fun addTeam(team: TeamDTO): Int {
        return transport.post("addTeam", Int.serializer(), "team" to team)
    }

    @RequireRole(Role.Admin)
    actual suspend fun addTeamMember(newTeamMember: TeamMemberDTO): Int {
        return transport.post("addTeamMember", Int.serializer(), "newTeamMember" to newTeamMember)
    }

    actual suspend fun getTeamById(id: Int): TeamDTO {
        return transport.get("getTeamById", TeamDTO.serializer(), "id" to id)
    }

    actual suspend fun getTeamByYear(year: String): TeamDTO {
        return transport.get("getTeamByYear", TeamDTO.serializer(), "year" to year)
    }

    actual suspend fun getAllTeamMembers(teamId: Int): List<TeamMemberDTO> {
        return transport.getList("getAllTeamMembers", TeamMemberDTO.serializer(), "name" to teamId.toString())
    }

    actual suspend fun getTeamMemberByTeamIdAndRole(role: String, teamId: Int): List<TeamMemberDTO> {
        return transport.getList("getTeamMemberByTeamIdAndRole", TeamMemberDTO.serializer(), "role" to role, "teamId" to teamId)
    }

    @RequireRole(Role.Admin)
    actual suspend fun editTeam(team: TeamDTO): Boolean {
        transport.post("editTeam", Boolean.serializer(), "team" to team)
        return true
    }

    @RequireRole(Role.Admin)
    actual suspend fun editTeamMember(teamMember: TeamMemberDTO): Boolean {
        return transport.post("editTeamMember", Boolean.serializer(), "teamMember" to teamMember)
    }

    @RequireRole(Role.Admin)
    actual suspend fun deleteTeam(teamId: Int): Boolean {
        transport.post("deleteTeam", Boolean.serializer(), "team" to teamId)
        return true
    }

    @RequireRole(Role.Admin)
    actual suspend fun deleteTeamMember(id: Int): Boolean {
        return transport.post("deleteTeamMember", Boolean.serializer(), "id" to id)
    }
}