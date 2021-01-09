package services

import model.Game

expect class GameService {
    suspend fun getAllGamesByYear(year: Int): List<Game>
    suspend fun addGameById(id: Int): Game
    suspend fun editGameById(id: Int): Game
    suspend fun deleteGameById(id: Int): Game
}