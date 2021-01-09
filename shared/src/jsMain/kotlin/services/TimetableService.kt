package services

import model.Workout

actual class TimetableService {

    actual suspend fun getWeekTimetable(): List<Workout> = TODO()
    actual suspend fun addWorkout(): Workout = TODO()
    actual suspend fun editWorkout(): Workout = TODO()
    actual suspend fun deleteWorkout(): Workout = TODO()

}