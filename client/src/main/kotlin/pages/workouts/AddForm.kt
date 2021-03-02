package pages.workouts

import Consts.workoutsInputs
import adminPageComponents.AdminButtonType
import adminPageComponents.FormComponent
import adminPageComponents.Input
import adminPageComponents.adminButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onSubmitFunction
import react.*
import services.WorkoutsService
import styled.styledDiv
import styled.styledForm

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