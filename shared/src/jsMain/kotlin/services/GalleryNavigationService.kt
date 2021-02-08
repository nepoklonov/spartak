package services

import kotlinx.serialization.builtins.serializer
import model.NavigationDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class GalleryNavigationService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)

    actual suspend fun getGalleryNavigationList(): List<NavigationDTO> {
        return transport.getList("getGalleryNavigationList", NavigationDTO.serializer())
    }

    actual suspend fun addGallerySection(navigationDTO: NavigationDTO): Int {
        return transport.post("addGallerySection", Int.serializer(), "navigationDTO" to navigationDTO)
    }

    actual suspend fun editGallerySection(navigationDTO: NavigationDTO): Boolean {
        return transport.post("editGallerySection", Boolean.serializer(), "navigationDTO" to navigationDTO)
    }

    actual suspend fun deleteGallerySection(id: Int): Boolean {
        return transport.post("deleteGallerySection", Boolean.serializer(), "id" to id)
    }
}