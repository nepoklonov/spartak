package services

import Annotations.RequireRole
import database.Trainers
import database.database
import model.TrainerDTO
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update
import rpc.RPCService

actual class TrainerService : RPCService {
    actual suspend fun getTrainerById(teamId: Int): TrainerDTO {
        return database {
            Trainers.select { Trainers.teamId eq teamId }.first().let {
                TrainerDTO(
                    it[Trainers.id].value,
                    it[Trainers.teamId],
                    it[Trainers.photo],
                    it[Trainers.name],
                    it[Trainers.dateOfBirth],
                    it[Trainers.info]
                )
            }
        }
    }

    private fun Trainers.insertTrainerDtoToDatabase(it: UpdateBuilder<Int>, trainer: TrainerDTO) {
        it[teamId] = trainer.teamId
        it[name] = trainer.name
        it[photo] = trainer.photo
        it[dateOfBirth] = trainer.dateOfBirth
        it[info] = trainer.info
    }

    @RequireRole(Role.Admin)
    actual suspend fun editTrainer(trainer: TrainerDTO): Boolean {
        database {
            Trainers.update({ Trainers.id eq trainer.id }) { insertTrainerDtoToDatabase(it, trainer) }
        }
        return true
    }

    @RequireRole(Role.Admin)
    actual suspend fun addTrainer(trainer: TrainerDTO): Int {
        return database {
            Trainers.insertAndGetId { insertTrainerDtoToDatabase(it, trainer) }
        }.value
    }

    @RequireRole(Role.Admin)
    actual suspend fun deleteTrainer(trainerId: Int): Boolean {
        database {
            Trainers.deleteWhere { Trainers.id eq trainerId }
        }
        return true
    }
}