package services

import kotlinx.serialization.builtins.serializer
import model.Check
import rpc.Transport
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

actual class AdminService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)
    actual suspend fun checkAdmin(login: String, password: String): Boolean {
        return transport.post("checkAdmin", Boolean.serializer(), "login" to login, "password" to password)
    }
}