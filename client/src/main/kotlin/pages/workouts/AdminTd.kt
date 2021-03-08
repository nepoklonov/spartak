package pages.workouts

import adminPageComponents.AdminButtonType
import adminPageComponents.adminButton
import consts.Input
import consts.monday
import consts.workoutsInputs
import isAdmin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onSubmitFunction
import model.WorkoutDTO
import pageComponents.FormState
import pageComponents.formComponent
import react.RBuilder
import react.RComponent
import react.RProps
import react.setState
import services.WorkoutsService
import styled.styledForm
import styled.styledTd

external interface AdminTdProps : RProps {
    var coroutineScope: CoroutineScope
    var workout: WorkoutDTO
    var selectedWorkout: String
}

class AdminTdState : FormState {
    override var inputs: MutableMap<String, Input> = workoutsInputs
    var editWorkoutForm: WorkoutDTO? = null
}

class AdminTd: RComponent<AdminTdProps, AdminTdState>() {
    init{
        state.inputs = workoutsInputs
        state.editWorkoutForm = null
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun RBuilder.render() {
        styledTd {
            if (isAdmin) {
                adminButton(AdminButtonType.Delete) {
                    val timetableService = WorkoutsService(coroutineContext)
                    props.coroutineScope.launch {
                        timetableService.makeNotActual(props.workout.id!!, monday)
                    }
                }
                if (state.editWorkoutForm != props.workout) {
                    adminButton(AdminButtonType.Edit) {
                        setState {
                            editWorkoutForm = props.workout
                        }
                    }
                } else {
                    styledForm {
                        attrs.onSubmitFunction = { event ->
                            event.preventDefault()
                            event.stopPropagation()
                            val timetableService = WorkoutsService(coroutineContext)
                            props.coroutineScope.launch {
                                var formIsCompleted = true
                                state.inputs.values.forEach {
                                    if (it.isRed) {
                                        formIsCompleted = false
                                    }
                                }
                                if (formIsCompleted) {
                                    timetableService.makeNotActual(
                                        props.workout.id!!,
                                        monday
                                    )
                                    timetableService.addWorkout(
                                        generateWorkoutDTO(state.inputs, props.selectedWorkout)
                                    )
                                }
                            }
                        }
                        formComponent(this)
                    }
                }
            }
        }
    }
}


