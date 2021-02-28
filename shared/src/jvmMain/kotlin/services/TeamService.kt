package services

import database.TeamMembers
import database.Teams
import database.Teams.link
import database.Teams.name
import database.database
import model.NavigationDTO
import model.TeamDTO
import model.TeamMemberDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import rpc.RPCService

actual class TeamService : RPCService {
    private fun TeamMembers.getTeamMemberDtoFromDatabase(it: ResultRow): TeamMemberDTO {
        return TeamMemberDTO(
            it[TeamMembers.id].value,
            it[teamLink],
            it[number],
            it[photo],
            it[firstName],
            it[lastName],
            it[role],
            it[birthday],
            it[city],
            it[teamRole]
        )
    }

    private fun TeamMembers.insertTeamMemberDtoToDatabase(it: UpdateBuilder<Int>, newTeamMember: TeamMemberDTO) {
        it[teamLink] = newTeamMember.teamLink
        it[number] = newTeamMember.number
        it[photo] = newTeamMember.photo
        it[firstName] = newTeamMember.firstName
        it[lastName] = newTeamMember.lastName
        it[role] = newTeamMember.role
        it[birthday] = newTeamMember.birthday
        it[city] = newTeamMember.city
        it[teamRole] = newTeamMember.teamRole
    }

    private fun Teams.getTeamDTO(it: ResultRow): TeamDTO {
        return TeamDTO(
            it[Teams.id].value,
            it[name],
            it[link],
            it[isOur],
            it[year],
        )
    }

    private fun Teams.insertTeamDtoToDatabase(it: UpdateBuilder<Int>, team: TeamDTO) {
        it[name] = team.name
        it[link] = team.link.toString()
        it[isOur] = team.isOur
        it[year] = team.year.toString()
    }

//    @RequireRole(Role.Admin)
    actual suspend fun addTeam(team: TeamDTO): Int {
        return database {
            Teams.insertAndGetId { insertTeamDtoToDatabase(it, team) }
        }.value
    }

//    @RequireRole(Role.Admin)
    actual suspend fun addTeamMember(newTeamMember: TeamMemberDTO): Int {
        return database {
            TeamMembers.insertAndGetId { insertTeamMemberDtoToDatabase(it, newTeamMember) }
        }.value
    }

    actual suspend fun getNavigationList(): List<NavigationDTO> {
        val navigationList = mutableListOf<NavigationDTO>()
        database {
            Teams.select{Teams.isOur eq true}.orderBy(Teams.id to SortOrder.ASC).forEach() {
                navigationList += NavigationDTO(it[Teams.id].value, it[name], it[link])
            }
        }
        return navigationList
    }

    actual suspend fun getTeamById(id: Int): TeamDTO {
        return database {
            Teams.select { Teams.id eq id }.first().let { Teams.getTeamDTO(it) }
        }
    }

    actual suspend fun getTeamByLink(link: String): TeamDTO {
        return database {
            Teams.select { Teams.link eq link }.first().let {
                Teams.getTeamDTO(it)
            }
        }
    }

    actual suspend fun getTeamMemberByRoleAndTeam(role: String, teamLink: String): List<TeamMemberDTO> {
        val liftOfTeamMembers = mutableListOf<TeamMemberDTO>()
        database {
            TeamMembers.select {
                (TeamMembers.teamLink eq teamLink) and (TeamMembers.role eq role)
            }.orderBy(
                TeamMembers.id to SortOrder.ASC
            ).forEach {
                liftOfTeamMembers += TeamMembers.getTeamMemberDtoFromDatabase(it)
            }
        }
        return liftOfTeamMembers
    }

//    @RequireRole(Role.Admin)
    actual suspend fun editTeam(team: TeamDTO): Boolean {
        database {
            Teams.update({ Teams.id eq team.id }) { insertTeamDtoToDatabase(it, team) }
        }
        return true
    }

//    @RequireRole(Role.Admin)
    actual suspend fun editTeamMember(teamMember: TeamMemberDTO): Boolean {
        database {
            TeamMembers.update({ TeamMembers.id eq teamMember.id }) { insertTeamMemberDtoToDatabase(it, teamMember) }
        }
        return true
    }

//    @RequireRole(Role.Admin)
    actual suspend fun deleteTeam(teamId: Int): Boolean {
        database {
            Teams.deleteWhere { Teams.id eq teamId }
        }
        return true
    }

//    @RequireRole(Role.Admin)
    actual suspend fun deleteTeamMember(id: Int): Boolean {
        database {
            TeamMembers.deleteWhere { TeamMembers.id eq id }
        }
        return true
    }
}