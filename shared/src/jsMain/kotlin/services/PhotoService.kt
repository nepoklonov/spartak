package services

import Annotations.RequireRole
import kotlinx.serialization.builtins.serializer
import model.PhotoDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class PhotoService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)

    actual suspend fun getAllPhotosBySection(section: String): List<PhotoDTO> {
        return transport.getList("getAllPhotosBySection", PhotoDTO.serializer(), "section" to section)
    }

    @RequireRole(Role.Admin)
    actual suspend fun addPhoto(photo: PhotoDTO): Int {
        return transport.post("addPhoto", Int.serializer(), "photo" to photo)
    }

    @RequireRole(Role.Admin)
    actual suspend fun deletePhoto(id: Int): Boolean {
        return transport.post("deletePhoto", Boolean.serializer(), "id" to id)
    }
}