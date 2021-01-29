package services

import model.TeamDTO
import model.TeamMemberDTO

expect class TeamService {
    suspend fun addTeam(team: TeamDTO): Int
    suspend fun addTeamMember(newTeamMember: TeamMemberDTO): Int
    suspend fun getTeamById(id: Int): TeamDTO
    suspend fun getAllTeamsByYear(year: String): List<TeamDTO>
    suspend fun getAllTeamMembers(teamId: Int): List<TeamMemberDTO>
    suspend fun getTeamMemberByRole(role: String): List<TeamMemberDTO>
    suspend fun editTeam(team: TeamDTO): Boolean
    suspend fun editTeamMember(teamMember: TeamMemberDTO): Boolean
    suspend fun deleteTeam(teamId: Int): Boolean
    suspend fun deleteTeamMember(id: Int): Boolean
}