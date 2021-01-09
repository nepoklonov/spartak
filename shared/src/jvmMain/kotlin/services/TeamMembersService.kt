package services

import model.TeamMember

actual class TeamMembersService {
    actual suspend fun getTeamMemberById(id: Int): TeamMember = TODO()
    actual suspend fun addTeamMember(): TeamMember = TODO()
    actual suspend fun editTeamMemberById(id: Int): TeamMember = TODO()
    actual suspend fun deleteTeamMemberById(id: Int): TeamMember = TODO()
}