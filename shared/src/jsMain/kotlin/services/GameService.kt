package services

import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import model.GameDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class GameService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)

    actual suspend fun getAllGamesByYear(year: Int): List<GameDTO> {
        return transport.getList("getAllGamesByYear", GameDTO.serializer(), "year" to  year.toString())
    }
    actual suspend fun addGame(newGame: GameDTO): Int {
        val newGameString = Json.encodeToString(GameDTO.serializer(), newGame)
        return transport.post("addGame", Int.serializer(), "newGame" to newGameString)
    }
    actual suspend fun editGame(game: GameDTO): Boolean {
        val gameString = Json.encodeToString(GameDTO.serializer(), game)
        return transport.post("editGame", Boolean.serializer(), "game" to gameString)
    }
    actual suspend fun deleteGameById(id: Int): Boolean {
        return transport.post("deleteGameById", Boolean.serializer(), "id" to id.toString())
    }
}