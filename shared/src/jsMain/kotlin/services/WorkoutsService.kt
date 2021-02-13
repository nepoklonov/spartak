package services

import Annotations.RequireRole
import Role
import kotlinx.serialization.builtins.serializer
import model.WorkoutDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class WorkoutsService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)

    actual suspend fun getWeekWorkoutsBySection(beginningOfTheWeek: Double, endOfTheWeek: Double, sectionLink: String): List<WorkoutDTO> {
        return transport.getList("getWeekWorkoutsBySection", WorkoutDTO.serializer(),
                "beginningOfTheWeek" to beginningOfTheWeek, "endOfTheWeek" to endOfTheWeek, "sectionLink" to sectionLink)
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