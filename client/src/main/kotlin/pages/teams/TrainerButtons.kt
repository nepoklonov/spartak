package pages.teams

import adminPageComponents.AdminButtonType
import adminPageComponents.FormComponent
import adminPageComponents.Input
import adminPageComponents.adminButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onSubmitFunction
import model.TrainerDTO
import react.*
import services.TrainerService
import styled.styledForm

external interface TrainerButtonsProps : RProps {
    var coroutineScope: CoroutineScope
    var trainer: TrainerDTO
    var selectedTeam: String
}

class TrainerButtonsState : RState {
    var trainerInputs: MutableMap<String, Input> = Consts.trainerInputs
    var editTrainerForm: TrainerDTO? = null
}

class TrainerButtons : RComponent<TrainerButtonsProps, TrainerButtonsState>() {
    init {
        state = TrainerButtonsState()
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
                    state.trainerInputs["name"]!!.inputValue = props.trainer.name
                    state.trainerInputs["info"]!!.inputValue = props.trainer.info
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
                        state.trainerInputs.values.forEach {
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
                                    state.trainerInputs["name"]!!.inputValue,
                                    state.trainerInputs["info"]!!.inputValue,
                                )
                            )
                        }
                    }
                }
                child(FormComponent::class) {
                    attrs.inputs = state.trainerInputs
                    attrs.updateState = { key: String, value: String, isRed: Boolean ->
                        setState {
                            state.trainerInputs[key]!!.inputValue = value
                            state.trainerInputs[key]!!.isRed = isRed
                        }
                    }
                }
            }
        }

    }
}
