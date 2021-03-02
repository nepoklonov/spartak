package pages.games

import Consts.gameInputs
import adminPageComponents.AdminButtonType
import adminPageComponents.FormComponent
import adminPageComponents.Input
import adminPageComponents.adminButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onSubmitFunction
import react.*
import services.GameService
import styled.styledDiv
import styled.styledForm

external interface AdminTdProps : RProps {
    var coroutineScope: CoroutineScope
    var game: GameWithTeams
    var selectedChampionship: String
}

class AdminTdState : RState {
    var inputs: MutableMap<String, Input> = gameInputs
    var editGameForm: GameWithTeams? = null
}

class AdminTd : RComponent<AdminTdProps, AdminTdState>() {
    init {
        state = AdminTdState()
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun RBuilder.render() {

        adminButton(AdminButtonType.Delete) {
            val gameService = GameService(coroutineContext)
            props.coroutineScope.launch {
                gameService.deleteGame(props.game.id)
            }
        }
        if (state.editGameForm != props.game) {
            adminButton(AdminButtonType.Edit) {
                setState {
                    editGameForm = props.game
                    inputs["date"]!!.inputValue = props.game.date
                    inputs["time"]!!.inputValue = props.game.time ?: ""
                    inputs["teamA"]!!.inputValue = props.game.teamA?.link ?: ""
                    inputs["teamB"]!!.inputValue = props.game.teamB?.link ?: ""
                    inputs["stadium"]!!.inputValue = props.game.stadium
                    inputs["result"]!!.inputValue = props.game.result ?: ""
                }
            }
        } else {
            styledDiv {
                styledForm {
                    attrs.onSubmitFunction = { event ->
                        event.preventDefault()
                        event.stopPropagation()
                        val gameService = GameService(coroutineContext)
                        props.coroutineScope.launch {
                            var formIsCompleted = true
                            state.inputs.values.forEach {
                                if (it.isRed) {
                                    formIsCompleted = false
                                }
                            }
                            if (formIsCompleted) {
                                gameService.editGame(
                                    generateGameDTO(state.inputs, props.game.id, props.selectedChampionship)
                                )
                            }
                        }
                    }
                    child(FormComponent::class) {
                        console.log(state.inputs)
                        attrs.inputs = state.inputs
                        attrs.updateState =
                            { key: String, value: String, isRed: Boolean ->
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