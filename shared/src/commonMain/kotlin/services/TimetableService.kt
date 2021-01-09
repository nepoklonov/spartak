package services

import model.Workout

expect class TimetableService {
    suspend fun getWeekTimetable(): List<Workout>
    suspend fun addWorkout(): Workout
    suspend fun editWorkout(): Workout
    suspend fun deleteWorkout(): Workout
}