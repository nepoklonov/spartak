package services

import model.WorkoutDTO

expect class WorkoutsService {
    suspend fun getWeekWorkoutsBySection(beginningOfTheWeek: Double, endOfTheWeek: Double, sectionLink: String): List<WorkoutDTO>
    suspend fun addWorkout(newWorkout: WorkoutDTO): Int
    suspend fun editWorkout(workout: WorkoutDTO): Boolean
    suspend fun deleteWorkout(id: Int): Boolean
}