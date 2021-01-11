package services

import model.TeamMemberDTO

expect class TeamMembersService {
    suspend fun getTeamMemberById(id: Int): TeamMemberDTO
    suspend fun addTeamMember(newTeamMember: TeamMemberDTO): Int
    suspend fun editTeamMember(teamMember: TeamMemberDTO): Boolean
    suspend fun deleteTeamMemberById(id: Int): Boolean
}