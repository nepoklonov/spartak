package services

import model.TeamDTO

expect class TeamService {
    suspend fun getTeam(name: String): TeamDTO
    suspend fun getTeamByYear(year: Int): List<TeamDTO>
    suspend fun editTeam(team: TeamDTO): Boolean
    suspend fun addTeam(team: TeamDTO): Int // return new team id
    suspend fun deleteTeam(team: TeamDTO): Boolean
}