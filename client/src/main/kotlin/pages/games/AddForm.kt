package pages.games

import adminPageComponents.AdminButtonType
import adminPageComponents.FormComponent
import adminPageComponents.Input
import adminPageComponents.adminButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onSubmitFunction
import model.TeamDTO
import react.*
import services.GameService
import services.TeamService
import styled.styledForm

external interface AddFormProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedChampionship: String
}

class AddFormState : RState {
    var addGameForm: Boolean = false
    var inputs: MutableMap<String, Input> = Consts.gameInputs
}

class AddForm: RComponent<AddFormProps, AddFormState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    init {
        state = AddFormState()
    }

    override fun RBuilder.render() {
        if (!state.addGameForm) {
            adminButton(AdminButtonType.Add) {
                setState {
                    addGameForm = true
                }
            }
        } else {
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
                            gameService.addGame(
                                generateGameDTO(state.inputs, null, props.selectedChampionship)
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
                    attrs.addOtherOption = { isItTeamA: Boolean, teamName: String ->
                        val teamService = TeamService(coroutineContext)
                        props.coroutineScope.launch {
                            val team = TeamDTO(
                                null,
                                teamName,
                                null,
                                false,
                                null
                            )
                            val id = teamService.addTeam(team)
                            team.id = id
                            setState {
                                if (isItTeamA) {
                                    inputs["teamA"]!!.inputValue = id.toString()
                                } else {
                                    inputs["teamB"]!!.inputValue = id.toString()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}