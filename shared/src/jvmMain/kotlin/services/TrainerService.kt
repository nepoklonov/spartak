package services

import model.TrainerDTO
import rpc.RPCService

actual class TrainerService : RPCService {
    actual suspend fun getTrainer(name: String): TrainerDTO {
        TODO("Not yet implemented")
    }
    actual suspend fun editTrainer(trainer: TrainerDTO): Boolean = TODO()
    actual suspend fun addTrainer(trainer: TrainerDTO): Int = TODO()
    actual suspend fun deleteTrainer(trainer: TrainerDTO): Boolean = TODO()
}