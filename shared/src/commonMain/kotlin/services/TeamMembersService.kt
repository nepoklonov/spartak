package services

import model.TeamMemberDTO

expect class TeamMembersService {
    suspend fun getTeamMemberById(id: String): TeamMemberDTO
    suspend fun addTeamMember(newTeamMember: String): Int
    suspend fun editTeamMember(teamMember: String): Boolean
    suspend fun deleteTeamMemberById(id: String): Boolean
}