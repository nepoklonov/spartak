package pages.teams

import adminPageComponents.AdminButtonType
import adminPageComponents.adminButton
import consts.Input
import consts.teamMemberInputs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onSubmitFunction
import model.TeamMemberDTO
import pageComponents.FormState
import pageComponents.formComponent
import react.RBuilder
import react.RComponent
import react.RProps
import react.setState
import services.TeamService
import styled.styledForm

external interface AddTeamMemberFormProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedTeam: String
}

class AddTeamMemberFormState : FormState {
    var addTeamMemberForm: Boolean = false
    override var inputs: MutableMap<String, Input> = teamMemberInputs
}

class AddTeamMemberForm : RComponent<AddTeamMemberFormProps, AddTeamMemberFormState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    init {
        state.addTeamMemberForm = false
        state.inputs = teamMemberInputs
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
                console.log(state.inputs)
                attrs.onSubmitFunction = { event ->
                    event.preventDefault()
                    event.stopPropagation()
                    val teamService = TeamService(coroutineContext)
                    props.coroutineScope.launch {
                        var formIsCompleted = true
                        state.inputs.values.forEach {
                            if (it.isRed) {
                                formIsCompleted = false
                            }
                        }
                        if (formIsCompleted) {
                            teamService.addTeamMember(
                                TeamMemberDTO(
                                    null,
                                    props.selectedTeam,
                                    state.inputs["number"]!!.inputValue,
                                    "address.png",
                                    state.inputs["firstName"]!!.inputValue,
                                    state.inputs["lastName"]!!.inputValue,
                                    state.inputs["role"]!!.inputValue,
                                    state.inputs["birthday"]!!.inputValue,
                                    state.inputs["city"]!!.inputValue,
                                    state.inputs["teamRole"]!!.inputValue
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
