package services

import model.Trainer
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class TrainerService (coroutineContext: CoroutineContext){
    private val transport = Transport(coroutineContext)
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