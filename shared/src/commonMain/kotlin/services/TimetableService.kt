package services

import model.WorkoutDTO

expect class TimetableService {
    suspend fun getWeekTimetable(beginningOfTheWeek: Int, endOfTheWeek: Int): List<WorkoutDTO>
    suspend fun addWorkout(newWorkout: WorkoutDTO): Int
    suspend fun editWorkout(workout: WorkoutDTO): Boolean
    suspend fun deleteWorkout(id: Int): Boolean
}