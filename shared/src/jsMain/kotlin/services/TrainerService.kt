package services

import kotlinx.serialization.builtins.serializer
import model.TrainerDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class TrainerService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)
    actual suspend fun getTrainerByLink(teamLink: String): TrainerDTO {
        return transport.get("getTrainerByLink", TrainerDTO.serializer(), "teamLink" to teamLink)
    }

    @RequireRole(Role.Admin)
    actual suspend fun editTrainer(trainer: TrainerDTO): Boolean {
        return transport.post("editTrainer", Boolean.serializer(), "trainer" to trainer)
    }

    @RequireRole(Role.Admin)
    actual suspend fun addTrainer(trainer: TrainerDTO): Int {
        return transport.post("addTrainer", Int.serializer(),"trainer" to trainer)
    }

    @RequireRole(Role.Admin)
    actual suspend fun deleteTrainer(trainerId: Int): Boolean {
        return transport.post("deleteTrainer", Boolean.serializer(),"trainerId" to trainerId)
    }
}