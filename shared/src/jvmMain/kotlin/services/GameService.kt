package services

import model.GameDTO

actual class GameService {
    actual suspend fun getAllGamesByYear(year: Int): List<GameDTO> = TODO()
    actual suspend fun addGame(newGame: GameDTO): Int = TODO()
    actual suspend fun editGame(game: GameDTO): Boolean = TODO()
    actual suspend fun deleteGameById(id: Int): Boolean = TODO()
}