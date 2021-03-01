package pages.WorkoutsAdminFunctions

import Consts.monday
import Consts.workoutsInputs
import adminPageComponents.AdminButtonType
import adminPageComponents.FormComponent
import adminPageComponents.Input
import adminPageComponents.adminButton
import isAdmin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onSubmitFunction
import model.WorkoutDTO
import react.*
import services.WorkoutsService
import styled.styledDiv
import styled.styledForm
import styled.styledTd
import kotlin.js.Date

external interface AddFormProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedWorkout: String
}

class AddFormState : RState {
    var addForm: Boolean = false
    var inputs: MutableMap<String, Input> = workoutsInputs
}

class AddForm: RComponent<AddFormProps, AddFormState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    init {
        state = AddFormState()
    }

    private fun RBuilder.addForm() {
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
                        timetableService.addWorkout(
                            generateWorkoutDTO(state.inputs, props.selectedWorkout)
                        )
                    }
                }
            }
            child(FormComponent::class) {
                attrs.inputs = state.inputs
                attrs.updateState = { key: String, value: String, isRed: Boolean ->
                    setState {
                        state.inputs[key]!!.inputValue = value
                        state.inputs[key]!!.isRed = isRed
                    }
                }
            }
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            if (!state.addForm) {
                adminButton(AdminButtonType.Add) {
                    setState {
                        addForm = true
                    }
                }
            } else {
                addForm()
            }
        }
    }
}

fun generateWorkoutDTO(inputs: MutableMap<String, Input>, selectedWorkout: String): WorkoutDTO {
    return WorkoutDTO(
        null,
        Date(inputs["startDatetime"]!!.inputValue).getHours()
            .toString()
                + ":"
                + Date(inputs["startDatetime"]!!.inputValue).getMinutes()
            .toString(),
        Date(inputs["endDatetime"]!!.inputValue).getHours().toString()
                + ":"
                + Date(inputs["endDatetime"]!!.inputValue).getMinutes()
            .toString(),
        Date(inputs["startDatetime"]!!.inputValue).getDay(),
        selectedWorkout,
        inputs["text"]!!.inputValue,
        monday,
        9999999999999.0
    )
}


external interface AdminTdProps : RProps {
    var coroutineScope: CoroutineScope
    var workout: WorkoutDTO
    var selectedWorkout: String
}

class AdminTdState : RState {
    var inputs: MutableMap<String, Input> = workoutsInputs
    var editWorkoutForm: WorkoutDTO? = null
}

class AdminTd: RComponent<AdminTdProps, AdminTdState>() {
    init{
        state = AdminTdState()
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
                        child(FormComponent::class) {
                            attrs.inputs = state.inputs
                            attrs.updateState = { key: String, value: String, isRed: Boolean ->
                                setState {
                                    state.inputs[key]!!.inputValue = value
                                    state.inputs[key]!!.isRed = isRed
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


