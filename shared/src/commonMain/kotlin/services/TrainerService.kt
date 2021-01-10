package services

import model.Trainer

expect class TrainerService {
    suspend fun getTrainer(name: String): Trainer
    suspend fun editTrainer(trainer: Trainer): Unit
    suspend fun addTrainer(trainer: Trainer): Unit
    suspend fun deleteTrainer(trainer: Trainer): Unit

}