package services

import model.TrainerDTO

expect class TrainerService {
    suspend fun getTrainerByLink(teamLink: String): TrainerDTO
    suspend fun editTrainer(trainer: TrainerDTO): Boolean
    suspend fun addTrainer(trainer: TrainerDTO): Int
    suspend fun deleteTrainer(trainerId: Int): Boolean

}