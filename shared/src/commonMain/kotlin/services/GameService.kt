package services

import model.GameDTO

expect class GameService {
    suspend fun getAllGamesByYear(year: String): List<GameDTO>
    suspend fun addGame(newGame: GameDTO): Int
    suspend fun editGame(game: GameDTO): Boolean
    suspend fun deleteGame(id: Int): Boolean
}