package services

import model.NavigationDTO
import model.TeamDTO
import model.TeamMemberDTO

expect class TeamService {
    suspend fun addTeam(team: TeamDTO): Int
    suspend fun addTeamMember(newTeamMember: TeamMemberDTO): Int
    suspend fun getNavigationList(): List<NavigationDTO>
    suspend fun getTeamById(id: Int): TeamDTO
    suspend fun getTeamByLink(link: String): TeamDTO
    suspend fun getAllTeamMembers(teamId: Int): List<TeamMemberDTO>
    suspend fun getTeamMemberByTeamIdAndRole(role: String, teamId: Int): List<TeamMemberDTO>
    suspend fun editTeam(team: TeamDTO): Boolean
    suspend fun editTeamMember(teamMember: TeamMemberDTO): Boolean
    suspend fun deleteTeam(teamId: Int): Boolean
    suspend fun deleteTeamMember(id: Int): Boolean
}