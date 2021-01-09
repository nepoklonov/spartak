package services

import model.Team

expect class TeamService {
    suspend fun getTeam(name: String): Team
    suspend fun editTeam(team: Team): Unit
    suspend fun addTeam(team: Team): Unit
    suspend fun deleteTeam(team: Team): Unit

}