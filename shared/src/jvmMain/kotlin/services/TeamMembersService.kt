package services

import model.TeamMemberDTO

actual class TeamMembersService {
    actual suspend fun getTeamMemberById(id: Int): TeamMemberDTO = TODO()
    actual suspend fun addTeamMember(newTeamMember: TeamMemberDTO): Int = TODO()
    actual suspend fun editTeamMember(teamMember: TeamMemberDTO): Boolean = TODO()
    actual suspend fun deleteTeamMemberById(id: Int): Boolean = TODO()
}