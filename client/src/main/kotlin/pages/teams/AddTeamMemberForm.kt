package pages.teams

import adminPageComponents.AdminButtonType
import adminPageComponents.FormComponent
import adminPageComponents.Input
import adminPageComponents.adminButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onSubmitFunction
import model.TeamMemberDTO
import react.*
import services.TeamService
import styled.styledForm

external interface AddTeamMemberFormProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedTeam: String
}

class AddTeamMemberFormState : RState {
    var addTeamMemberForm: Boolean = false
    var teamMemberInputs: MutableMap<String, Input> = Consts.teamMemberInputs
}

class AddTeamMemberForm : RComponent<AddTeamMemberFormProps, AddTeamMemberFormState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    init {
        state = AddTeamMemberFormState()
    }

    override fun RBuilder.render() {
        if (!state.addTeamMemberForm) {
            adminButton(AdminButtonType.Add) {
                setState {
                    addTeamMemberForm = true
                }
            }
        } else {
            styledForm {
                attrs.onSubmitFunction = { event ->
                    console.log(state.teamMemberInputs)
                    event.preventDefault()
                    event.stopPropagation()
                    val teamService = TeamService(coroutineContext)
                    props.coroutineScope.launch {
                        var formIsCompleted = true
                        state.teamMemberInputs.values.forEach {
                            if (it.isRed) {
                                formIsCompleted = false
                            }
                        }
                        if (formIsCompleted) {
                            teamService.addTeamMember(
                                TeamMemberDTO(
                                    null,
                                    props.selectedTeam,
                                    state.teamMemberInputs["number"]!!.inputValue,
                                    "address.png",
                                    state.teamMemberInputs["firstName"]!!.inputValue,
                                    state.teamMemberInputs["lastName"]!!.inputValue,
                                    state.teamMemberInputs["role"]!!.inputValue,
                                    state.teamMemberInputs["birthday"]!!.inputValue,
                                    state.teamMemberInputs["city"]!!.inputValue,
                                    state.teamMemberInputs["teamRole"]!!.inputValue
                                )
                            )
                        }
                    }
                }
                child(FormComponent::class) {
                    attrs.inputs = state.teamMemberInputs
                    attrs.updateState = { key: String, value: String, isRed: Boolean ->
                        setState {
                            state.teamMemberInputs[key]!!.inputValue = value
                            state.teamMemberInputs[key]!!.isRed = isRed
                        }
                    }
                }
            }
        }
    }
}
