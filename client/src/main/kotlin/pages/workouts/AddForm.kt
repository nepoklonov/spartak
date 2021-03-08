package pages.workouts

import adminPageComponents.AdminButtonType
import adminPageComponents.adminButton
import consts.Input
import consts.workoutsInputs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onSubmitFunction
import pageComponents.FormState
import pageComponents.formComponent
import react.RBuilder
import react.RComponent
import react.RProps
import react.setState
import services.WorkoutsService
import styled.styledDiv
import styled.styledForm

external interface AddFormProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedWorkout: String
}

class AddFormState : FormState {
    var addForm: Boolean = false
    override var inputs: MutableMap<String, Input> = workoutsInputs
}

class AddForm: RComponent<AddFormProps, AddFormState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    init {
        state.addForm = false
        state.inputs = workoutsInputs
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
            formComponent(this)
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