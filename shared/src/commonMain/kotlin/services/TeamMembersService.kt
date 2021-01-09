package services

import model.TeamMember

expect class TeamMembersService {
    suspend fun getTeamMemberById(id: Int): TeamMember
    suspend fun addTeamMember(): TeamMember
    suspend fun editTeamMemberById(id: Int): TeamMember
    suspend fun deleteTeamMemberById(id: Int): TeamMember
}