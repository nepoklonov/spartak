package services

import database.TeamMembers
import database.database
import model.TeamMemberDTO
import org.jetbrains.exposed.sql.selectAll
import rpc.RPCService

actual class TeamMembersService : RPCService{
    actual suspend fun getTeamMemberById(id: String): TeamMemberDTO{
        return database {
            TeamMembers.selectAll().first().let {
                TeamMemberDTO(it[TeamMembers.id].value,
                        it[TeamMembers.teamId],
                        it[TeamMembers.firstName],
                        it[TeamMembers.lastName],
                        it[TeamMembers.role],
                        it[TeamMembers.birthday],
                        it[TeamMembers.city]
                )
            }
        }
    }
    actual suspend fun addTeamMember(newTeamMember: TeamMemberDTO): Int = TODO()
    actual suspend fun editTeamMember(teamMember: TeamMemberDTO): Boolean = TODO()
    actual suspend fun deleteTeamMemberById(id: Int): Boolean = TODO()
}