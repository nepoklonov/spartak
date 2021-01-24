package services

import model.TeamDTO
import model.TeamMemberDTO

expect class TeamService {
    suspend fun addTeam(team: TeamDTO): Int
    suspend fun addTeamMember(newTeamMember: TeamMemberDTO): Int
    suspend fun getTeam(name: String): TeamDTO
    suspend fun getAllTeamsByYear(year: String): List<TeamDTO>
    suspend fun getAllTeamMembers(teamId: Int): List<TeamMemberDTO>
    suspend fun getTeamMemberById(id: Int): TeamMemberDTO
    suspend fun editTeam(team: TeamDTO): Boolean
    suspend fun editTeamMember(teamMember: String): Boolean
    suspend fun deleteTeamById(teamId: Int): Boolean
    suspend fun deleteTeamMemberById(id: Int): Boolean
}