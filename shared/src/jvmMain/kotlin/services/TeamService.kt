package services

import database.TeamMembers
import database.Teams
import database.database
import model.TeamDTO
import model.TeamMemberDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import rpc.RPCService

actual class TeamService : RPCService {
    private fun TeamMembers.insertTeamMemberDtoToDatabase(it: UpdateBuilder<Int>, newTeamMember: TeamMemberDTO) {
        it[teamId] = newTeamMember.teamId
        it[firstName] = newTeamMember.firstName
        it[lastName] = newTeamMember.lastName
        it[role] = newTeamMember.role
        it[birthday] = newTeamMember.birthday
        it[city] = newTeamMember.city
    }

    private fun Teams.getTeamDTO(it: ResultRow): TeamDTO {
        return TeamDTO(
            it[Teams.id].value,
            it[name],
            it[isOur],
            it[type],
            it[year],
            it[trainerId]
        )
    }

    private fun Teams.insertTeamDtoToDatabase(it: UpdateBuilder<Int>, team: TeamDTO) {
        it[name] = team.name
        it[isOur] = team.isOur
        it[type] = team.type
        it[year] = team.year.toString()
        it[trainerId] = team.trainerId
    }

    private fun TeamMembers.getTeamMemberDtoFromDatabase(it: ResultRow): TeamMemberDTO {
        return TeamMemberDTO(
            it[TeamMembers.id].value,
            it[teamId],
            it[firstName],
            it[lastName],
            it[role],
            it[birthday],
            it[city]
        )
    }

    actual suspend fun addTeam(team: TeamDTO): Int {
        return database {
            Teams.insertAndGetId { insertTeamDtoToDatabase(it, team) }
        }.value
    }

    actual suspend fun addTeamMember(newTeamMember: TeamMemberDTO): Int {
        return database {
            TeamMembers.insertAndGetId { insertTeamMemberDtoToDatabase(it, newTeamMember) }
        }.value
    }

    actual suspend fun getTeam(name: String): TeamDTO {
        return database {
            Teams.selectAll().first().let { Teams.getTeamDTO(it) }
        }
    }

    actual suspend fun getAllTeamsByYear(year: String): List<TeamDTO> {
        val listOfTeams: MutableList<TeamDTO> = mutableListOf()
        database {
            Teams.select { Teams.year eq year }.forEach() {
                listOfTeams += Teams.getTeamDTO(it)
            }
        }
        return listOfTeams
    }

    actual suspend fun getAllTeamMembers(teamId: Int): List<TeamMemberDTO> {
        val listOfTeamMembers: MutableList<TeamMemberDTO> = mutableListOf()
        database {
            TeamMembers.select { TeamMembers.teamId eq teamId }.forEach {
                listOfTeamMembers += TeamMembers.getTeamMemberDtoFromDatabase(it)
            }
        }
        return listOfTeamMembers
    }

    actual suspend fun getTeamMemberById(id: Int): TeamMemberDTO {
        return database {
            TeamMembers.select { TeamMembers.id eq id }.single().let {
                TeamMembers.getTeamMemberDtoFromDatabase(it)
            }
        }
    }

    actual suspend fun editTeam(team: TeamDTO): Boolean {
        database {
            Teams.update({ Teams.id eq team.id }) { insertTeamDtoToDatabase(it, team) }
        }
        return true
    }

    actual suspend fun editTeamMember(teamMember: TeamMemberDTO): Boolean {
        database {
            TeamMembers.update({ TeamMembers.id eq teamMember.id }) { insertTeamMemberDtoToDatabase(it, teamMember) }
        }
        return true
    }

    actual suspend fun deleteTeamById(teamId: Int): Boolean {
        database {
            Teams.deleteWhere { Teams.id eq teamId }
        }
        return true
    }

    actual suspend fun deleteTeamMemberById(id: Int): Boolean {
        database {
            TeamMembers.deleteWhere { TeamMembers.id eq id }
        }
        return true
    }
}