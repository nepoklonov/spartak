package services

import kotlinx.serialization.builtins.serializer
import model.PhotoDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class PhotoService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)

    actual suspend fun getAllPhotosBySection(section: String): List<String> {
        return transport.getList("getAllPhotosBySection", String.serializer(), "section" to section)
    }

    actual suspend fun addPhoto(photo: PhotoDTO): Int {
        TODO("Not yet implemented")
    }

    actual suspend fun deletePhoto(id: Int): Boolean {
        TODO("Not yet implemented")
    }
}