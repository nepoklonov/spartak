package services

import database.Workouts
import database.database
import model.WorkoutDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import rpc.RPCService

actual class WorkoutsService: RPCService {
    actual suspend fun getWeekWorkoutsBySection(beginningOfTheWeek: Double, endOfTheWeek: Double, sectionLink: String): List<WorkoutDTO> {
        val listOfWorkoutDTO = mutableListOf<WorkoutDTO>()
        database {
            Workouts.select {
                (Workouts.sectionLink eq sectionLink) and (Workouts.datetime greaterEq beginningOfTheWeek) and (Workouts.datetime lessEq endOfTheWeek)
            }.orderBy(Workouts.datetime to SortOrder.ASC ).forEach() {
                listOfWorkoutDTO += WorkoutDTO(
                        it[Workouts.id].value,
                        it[Workouts.datetime],
                        it[Workouts.sectionLink],
                        it[Workouts.text]
                )
            }
        }
        return listOfWorkoutDTO
    }

    private fun Workouts.insertWorkoutDtoToDatabase(it: UpdateBuilder<Int>, workout: WorkoutDTO) {
        it[datetime] = workout.datetime
        it[sectionLink] = workout.sectionLink
        it[text] = workout.text
    }

    actual suspend fun addWorkout(newWorkout: WorkoutDTO): Int {
        return database {
            Workouts.insertAndGetId { insertWorkoutDtoToDatabase(it, newWorkout) }
        }.value
    }

    actual suspend fun editWorkout(workout: WorkoutDTO): Boolean {
        database {
            Workouts.update({ Workouts.id eq workout.id }) { insertWorkoutDtoToDatabase(it, workout) }
        }
        return true
    }

    actual suspend fun deleteWorkout(id: Int): Boolean {
        database {
            Workouts.deleteWhere { Workouts.id eq id }
        }
        return true
    }
}