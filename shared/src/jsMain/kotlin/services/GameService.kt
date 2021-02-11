package services

import Annotations.RequireRole
import kotlinx.serialization.builtins.serializer
import model.GameDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class GameService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)

    actual suspend fun getAllGamesByYear(year: String): List<GameDTO> {
        return transport.getList("getAllGamesByYear", GameDTO.serializer(), "year" to  year)
    }

    @RequireRole(Role.Admin)
    actual suspend fun addGame(newGame: GameDTO): Int {
        return transport.post("addGame", Int.serializer(), "newGame" to newGame)
    }

    @RequireRole(Role.Admin)
    actual suspend fun editGame(game: GameDTO): Boolean {
        return transport.post("editGame", Boolean.serializer(), "game" to game)
    }

    @RequireRole(Role.Admin)
    actual suspend fun deleteGame(id: Int): Boolean {
        return transport.post("deleteGame", Boolean.serializer(), "id" to id)
    }
}