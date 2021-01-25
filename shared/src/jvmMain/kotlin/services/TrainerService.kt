package services

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
    actual suspend fun getTrainer(name: String): TrainerDTO {
        return database {
            Trainers.select { Trainers.name eq name }.first().let {
                TrainerDTO(
                        it[Trainers.id].value,
                        it[Trainers.name],
                        it[Trainers.dateOfBirth],
                        it[Trainers.info]
                )
            }
        }
    }

    private fun Trainers.insertTrainerDtoToDatabase(it: UpdateBuilder<Int>, trainer: TrainerDTO) {
        it[name] = trainer.name
        it[dateOfBirth] = trainer.dateOfBirth
        it[info] = trainer.info
    }

    actual suspend fun editTrainer(trainer: TrainerDTO): Boolean {
        database {
            Trainers.update({ Trainers.id eq trainer.id }) { insertTrainerDtoToDatabase(it, trainer) }
        }
        return true
    }

    actual suspend fun addTrainer(trainer: TrainerDTO): Int {
        return database {
            Trainers.insertAndGetId { insertTrainerDtoToDatabase(it, trainer) }
        }.value
    }

    actual suspend fun deleteTrainerById(trainerId: Int): Boolean {
        database {
            Trainers.deleteWhere { Trainers.id eq trainerId}
        }
        return true
    }
}