package services

import model.Game

actual class GameService {
    actual suspend fun getAllGamesByYear(year: Int): List<Game> = TODO()
    actual suspend fun addGameById(id: Int): Game = TODO()
    actual suspend fun editGameById(id: Int): Game = TODO()
    actual suspend fun deleteGameById(id: Int): Game = TODO()
}