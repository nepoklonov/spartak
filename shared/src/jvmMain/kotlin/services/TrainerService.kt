package services

import model.Trainer
import rpc.RPCService

actual class TrainerService : RPCService {
    actual suspend fun getTrainer(name: String): Trainer {
        TODO("Not yet implemented")
    }

    actual suspend fun editTrainer(trainer: Trainer) {
    }

    actual suspend fun addTrainer(trainer: Trainer) {
    }

    actual suspend fun deleteTrainer(trainer: Trainer) {
    }

}