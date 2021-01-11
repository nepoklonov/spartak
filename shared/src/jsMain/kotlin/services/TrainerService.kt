package services

import model.TrainerDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class TrainerService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)
    actual suspend fun getTrainer(name: String): TrainerDTO {
        TODO("Not yet implemented")
    }
    actual suspend fun editTrainer(trainer: TrainerDTO): Boolean = TODO()
    actual suspend fun addTrainer(trainer: TrainerDTO): Int = TODO()
    actual suspend fun deleteTrainer(trainer: TrainerDTO): Boolean = TODO()
}