package pages.games

import adminPageComponents.AdminButtonType
import adminPageComponents.adminButton
import consts.Input
import consts.gameInputs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onSubmitFunction
import pageComponents.FormState
import pageComponents.formComponent
import react.RBuilder
import react.RComponent
import react.RProps
import react.setState
import services.GameService
import styled.styledDiv
import styled.styledForm

external interface AdminTdProps : RProps {
    var coroutineScope: CoroutineScope
    var game: GameWithTeams
    var selectedChampionship: String
}

class AdminTdState : FormState {
    override var inputs: MutableMap<String, Input> = gameInputs
    var editGameForm: GameWithTeams? = null
}

class AdminTd : RComponent<AdminTdProps, AdminTdState>() {
    init {
        state.inputs = gameInputs
        state.editGameForm = null
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
                    inputs["teamAId"]!!.inputValue = props.game.teamA?.link ?: ""
                    inputs["teamBId"]!!.inputValue = props.game.teamB?.link ?: ""
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
                    formComponent(this)
                }
            }
        }
    }
}