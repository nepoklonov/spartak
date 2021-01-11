package services

import model.WorkoutDTO

expect class TimetableService {
    suspend fun getWeekTimetable(): List<WorkoutDTO>
    suspend fun addWorkout(newWorkout: WorkoutDTO): Int
    suspend fun editWorkout(workout: WorkoutDTO): Boolean
    suspend fun deleteWorkoutById(id: Int): Boolean
}