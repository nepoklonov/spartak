package pages.teams

import adminPageComponents.AdminButtonType
import adminPageComponents.adminButton
import consts.Input
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onSubmitFunction
import model.TrainerDTO
import pageComponents.FormState
import pageComponents.formComponent
import react.RBuilder
import react.RComponent
import react.RProps
import react.setState
import services.TrainerService
import styled.styledForm

external interface TrainerButtonsProps : RProps {
    var coroutineScope: CoroutineScope
    var trainer: TrainerDTO
    var selectedTeam: String
}

class TrainerButtonsState : FormState {
    override var inputs: MutableMap<String, Input> = consts.trainerInputs
    var editTrainerForm: TrainerDTO? = null
}

class TrainerButtons : RComponent<TrainerButtonsProps, TrainerButtonsState>() {
    init {
        state.inputs = consts.trainerInputs
        state.editTrainerForm = null
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun RBuilder.render() {
        adminButton(AdminButtonType.Delete) {
            val trainerService = TrainerService(coroutineContext)
            props.coroutineScope.launch {
                trainerService.deleteTrainer(props.trainer.id!!)
            }
        }
        if (state.editTrainerForm != props.trainer) {
            adminButton(AdminButtonType.Edit) {
                setState {
                    editTrainerForm = props.trainer
                    state.inputs["name"]!!.inputValue = props.trainer.name
                    state.inputs["info"]!!.inputValue = props.trainer.info
                }
            }
        } else {
            styledForm {
                attrs.onSubmitFunction = { event ->
                    event.preventDefault()
                    event.stopPropagation()
                    val trainerService = TrainerService(coroutineContext)
                    props.coroutineScope.launch {
                        var formIsCompleted = true
                        state.inputs.values.forEach {
                            if (it.isRed) {
                                formIsCompleted = false
                            }
                        }
                        if (formIsCompleted) {
                            trainerService.editTrainer(
                                TrainerDTO(
                                    props.trainer.id,
                                    props.selectedTeam,
                                    "address.png",
                                    state.inputs["name"]!!.inputValue,
                                    state.inputs["info"]!!.inputValue,
                                )
                            )
                        }
                    }
                }
                formComponent(this)
            }
        }

    }
}
