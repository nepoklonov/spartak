package services

import database.TeamMembers
import database.database
import kotlinx.serialization.json.Json
import model.TeamMemberDTO
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update
import rpc.RPCService

actual class TeamMembersService : RPCService {
    actual suspend fun getTeamMemberById(id: String): TeamMemberDTO {
        return database {
            TeamMembers.select { TeamMembers.id eq id.toInt() }.single().let {
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

    private fun TeamMembers.insertTeamMemberDtoToDatabase(it: UpdateBuilder<Int>, newTeamMember: TeamMemberDTO) {
        it[teamId] = newTeamMember.teamId
        it[firstName] = newTeamMember.firstName
        it[lastName] = newTeamMember.lastName
        it[role] = newTeamMember.role
        it[birthday] = newTeamMember.birthday
        it[city] = newTeamMember.city
    }

    actual suspend fun addTeamMember(newTeamMember: String): Int {
        return database {
            TeamMembers.insertAndGetId { insertTeamMemberDtoToDatabase(it, Json.decodeFromString(TeamMemberDTO.serializer(), newTeamMember)) }
        }.value
    }


    actual suspend fun editTeamMember(teamMember: String): Boolean {
        database {
            TeamMembers.update({ TeamMembers.id eq id.toInt() }) { insertTeamMemberDtoToDatabase(it, Json.decodeFromString(TeamMemberDTO.serializer(), teamMember)) }
        }
        return true
    }

    actual suspend fun deleteTeamMemberById(id: String): Boolean{
        database {
            TeamMembers.deleteWhere { TeamMembers.id eq id.toInt() }
        }
        return true
    }
}