package services

import kotlinx.serialization.builtins.serializer
import model.NavigationDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class WorkoutsNavigationService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)

    actual suspend fun getWorkoutsNavigationList(): List<NavigationDTO> {
        return transport.getList("getWorkoutsNavigationList", NavigationDTO.serializer())
    }

    actual suspend fun addWorkoutsSection(navigationDTO: NavigationDTO): Int {
        return transport.post("addWorkoutsSection", Int.serializer(), "navigationDTO" to navigationDTO)
    }


    actual suspend fun editWorkoutsSection(navigationDTO: NavigationDTO): Boolean {
        return transport.post("editWorkoutsSection", Boolean.serializer(), "navigationDTO" to navigationDTO)
    }


    actual suspend fun deleteWorkoutsSection(id: Int): Boolean {
        return transport.post("deleteWorkoutsSection", Boolean.serializer(), "id" to id)
    }
}