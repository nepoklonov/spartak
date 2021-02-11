package services

import Annotations.RequireRole
import kotlinx.serialization.builtins.serializer
import model.WorkoutDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class TimetableService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)

    actual suspend fun getWeekTimetableByType(beginningOfTheWeek: Double, endOfTheWeek: Double, type: String): List<WorkoutDTO> {
        return transport.getList("getWeekTimetableByType", WorkoutDTO.serializer(),
                "beginningOfTheWeek" to beginningOfTheWeek, "endOfTheWeek" to endOfTheWeek, "type" to type)
    }

    @RequireRole(Role.Admin)
    actual suspend fun addWorkout(newWorkout: WorkoutDTO): Int {
        return transport.post("addWorkout", Int.serializer(), "newWorkout" to newWorkout)
    }

    @RequireRole(Role.Admin)
    actual suspend fun editWorkout(workout: WorkoutDTO): Boolean {
        return transport.post("editWorkout", Boolean.serializer(), "workout" to workout)
    }

    @RequireRole(Role.Admin)
    actual suspend fun deleteWorkout(id: Int): Boolean {
        return transport.post("deleteWorkout", Boolean.serializer(), "id" to id)
    }
}