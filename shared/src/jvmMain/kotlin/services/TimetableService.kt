package services

import database.Timetable
import database.database
import model.WorkoutDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import rpc.RPCService

actual class TimetableService: RPCService {
    actual suspend fun getWeekTimetableByType(beginningOfTheWeek: Double, endOfTheWeek: Double, type: String): List<WorkoutDTO> {
        val listOfWorkoutDTO = mutableListOf<WorkoutDTO>()
        database {
            Timetable.select {
                (Timetable.type eq type) and (Timetable.datetime greaterEq beginningOfTheWeek) and (Timetable.datetime lessEq endOfTheWeek)
            }.orderBy(Timetable.datetime to SortOrder.ASC ).forEach() {
                listOfWorkoutDTO += WorkoutDTO(
                        it[Timetable.id].value,
                        it[Timetable.datetime],
                        it[Timetable.teamLink],
                        it[Timetable.type],
                        it[Timetable.place]
                )
            }
        }
        return listOfWorkoutDTO
    }

    private fun Timetable.insertWorkoutDtoToDatabase(it: UpdateBuilder<Int>, workout: WorkoutDTO) {
        it[datetime] = workout.datetime
        it[teamLink] = workout.teamLink
        it[type] = workout.type
        it[place] = workout.place
    }

    actual suspend fun addWorkout(newWorkout: WorkoutDTO): Int {
        return database {
            Timetable.insertAndGetId { insertWorkoutDtoToDatabase(it, newWorkout) }
        }.value
    }

    actual suspend fun editWorkout(workout: WorkoutDTO): Boolean {
        database {
            Timetable.update({ Timetable.id eq workout.id }) { insertWorkoutDtoToDatabase(it, workout) }
        }
        return true
    }

    actual suspend fun deleteWorkout(id: Int): Boolean {
        database {
            Timetable.deleteWhere { Timetable.id eq id }
        }
        return true
    }
}