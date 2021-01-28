package services

import kotlinx.serialization.builtins.serializer
import model.GameDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class GameService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)

    actual suspend fun getAllGamesByYear(year: Int): List<GameDTO> {
        return transport.getList("getAllGamesByYear", GameDTO.serializer(), "year" to  year.toString())
    }
    actual suspend fun addGame(newGame: GameDTO): Int {
        return transport.post("addGame", Int.serializer(), "newGame" to newGame)
    }
    actual suspend fun editGame(game: GameDTO): Boolean {
        return transport.post("editGame", Boolean.serializer(), "game" to game)
    }
    actual suspend fun deleteGame(id: Int): Boolean {
        return transport.post("deleteGame", Boolean.serializer(), "id" to id)
    }
}