package services

import model.WorkoutDTO

actual class TimetableService {
    actual suspend fun getWeekTimetable(): List<WorkoutDTO> = TODO()
    actual suspend fun addWorkout(newWorkout: WorkoutDTO): Int = TODO()
    actual suspend fun editWorkout(workout: WorkoutDTO): Boolean = TODO()
    actual suspend fun deleteWorkoutById(id: Int): Boolean = TODO()
}