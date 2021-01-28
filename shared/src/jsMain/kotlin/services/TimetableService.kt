package services

import kotlinx.serialization.builtins.serializer
import model.WorkoutDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class TimetableService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)
    actual suspend fun getWeekTimetable(beginningOfTheWeek: Int, endOfTheWeek: Int): List<WorkoutDTO> {
        return transport.getList("getWeekTimetable", WorkoutDTO.serializer(),
                "beginningOfTheWeek" to beginningOfTheWeek, "endOfTheWeek" to endOfTheWeek)
    }
    actual suspend fun addWorkout(newWorkout: WorkoutDTO): Int {
        return transport.post("addWorkout", Int.serializer(), "newWorkout" to newWorkout)
    }
    actual suspend fun editWorkout(workout: WorkoutDTO): Boolean {
        return transport.post("editWorkout", Boolean.serializer(), "workout" to workout)
    }
    actual suspend fun deleteWorkout(id: Int): Boolean {
        return transport.post("deleteWorkout", Boolean.serializer(), "id" to id)
    }
}