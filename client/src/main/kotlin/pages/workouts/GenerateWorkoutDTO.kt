package pages.workouts

import consts.Input
import consts.monday
import model.WorkoutDTO
import kotlin.js.Date

fun generateWorkoutDTO(inputs: MutableMap<String, Input>, selectedWorkout: String): WorkoutDTO {
    return WorkoutDTO(
        null,
        Date(inputs["startDatetime"]!!.inputValue).getHours().toString()
                + ":"
                + Date(inputs["startDatetime"]!!.inputValue).getMinutes().toString(),
        Date(inputs["endDatetime"]!!.inputValue).getHours().toString()
                + ":"
                + Date(inputs["endDatetime"]!!.inputValue).getMinutes().toString(),
        Date(inputs["startDatetime"]!!.inputValue).getDay(),
        selectedWorkout,
        inputs["text"]!!.inputValue,
        monday,
        9999999999999.0
    )
}
