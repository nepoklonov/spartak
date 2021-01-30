package services

import kotlinx.serialization.builtins.serializer
import model.TrainerDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class TrainerService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)
    actual suspend fun getTrainerById(teamId: Int): TrainerDTO {
        return transport.get("getTrainerById", TrainerDTO.serializer(), "teamId" to teamId)
    }
    actual suspend fun editTrainer(trainer: TrainerDTO): Boolean {
        return transport.post("editTrainer", Boolean.serializer(), "trainer" to trainer)
    }
    actual suspend fun addTrainer(trainer: TrainerDTO): Int {
        return transport.post("addTrainer", Int.serializer(),"trainer" to trainer)
    }
    actual suspend fun deleteTrainer(trainerId: Int): Boolean {
        return transport.post("deleteTrainer", Boolean.serializer(),"trainerId" to trainerId)
    }
}