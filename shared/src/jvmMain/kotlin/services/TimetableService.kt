package services

import database.Timetable
import database.database
import model.WorkoutDTO
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import rpc.RPCService

actual class TimetableService: RPCService {
    actual suspend fun getWeekTimetable(beginningOfTheWeek: Int, endOfTheWeek: Int): List<WorkoutDTO> {
        val listOfWorkoutDTO = mutableListOf<WorkoutDTO>()
        database {
            Timetable.select {
                (Timetable.date greaterEq beginningOfTheWeek) and (Timetable.date lessEq endOfTheWeek)
            }.forEach() {
                listOfWorkoutDTO += WorkoutDTO(
                        it[Timetable.id].value,
                        it[Timetable.date],
                        it[Timetable.time],
                        it[Timetable.teamId],
                        it[Timetable.type],
                        it[Timetable.place]
                )
            }
        }
        return listOfWorkoutDTO
    }

    private fun Timetable.insertWorkoutDtoToDatabase(it: UpdateBuilder<Int>, workout: WorkoutDTO) {
        it[date] = workout.date
        it[time] = workout.time
        it[teamId] = workout.teamId
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