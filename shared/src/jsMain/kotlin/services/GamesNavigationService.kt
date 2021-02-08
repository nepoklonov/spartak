package services

import kotlinx.serialization.builtins.serializer
import model.NavigationDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class GamesNavigationService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)

    actual suspend fun getGamesNavigationList(): List<NavigationDTO> {
        return transport.getList("getGamesNavigationList", NavigationDTO.serializer())
    }

    actual suspend fun addGamesSection(navigationDTO: NavigationDTO): Int {
        return transport.post("addGamesSection", Int.serializer(), "navigationDTO" to navigationDTO)
    }

    actual suspend fun editGamesSection(navigationDTO: NavigationDTO): Boolean {
        return transport.post("editGamesSection", Boolean.serializer(), "navigationDTO" to navigationDTO)
    }

    actual suspend fun deleteGamesSection(id: Int): Boolean {
        return transport.post("deleteGamesSection", Boolean.serializer(), "id" to id)
    }
}