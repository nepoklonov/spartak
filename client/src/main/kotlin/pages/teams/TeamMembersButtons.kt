package pages.teams

import adminPageComponents.AdminButtonType
import adminPageComponents.adminButton
import consts.Input
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

external interface TeamMembersButtonsProps : RProps {
    var coroutineScope: CoroutineScope
    var teamMember: TeamMemberDTO
    var selectedTeam: String
}

class TeamMembersButtonsState : FormState {
    override var inputs: MutableMap<String, Input> = consts.teamMemberInputs
    var editTeamMemberForm: TeamMemberDTO? = null
}

class TeamMembersButtons: RComponent<TeamMembersButtonsProps, TeamMembersButtonsState>() {
    init{
        state.inputs = consts.teamMemberInputs
        state.editTeamMemberForm = null
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun RBuilder.render(){
        adminButton(AdminButtonType.Delete) {
            val teamService = TeamService(coroutineContext)
            props.coroutineScope.launch {
                teamService.deleteTeamMember(props.teamMember.id!!)
            }
        }

        if (state.editTeamMemberForm != props.teamMember) {
            adminButton(AdminButtonType.Edit) {
                setState {
                    editTeamMemberForm = props.teamMember
                    inputs["number"]!!.inputValue = props.teamMember.number
                    inputs["firstName"]!!.inputValue = props.teamMember.firstName
                    inputs["lastName"]!!.inputValue = props.teamMember.lastName
                    inputs["role"]!!.inputValue = props.teamMember.role
                    inputs["birthday"]!!.inputValue = props.teamMember.birthday
                    inputs["city"]!!.inputValue = props.teamMember.city
                    inputs["teamRole"]!!.inputValue = props.teamMember.number
                }
            }
        } else {
            styledForm {
                attrs.onSubmitFunction = { event ->
                    console.log(state.inputs)
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
                            teamService.editTeamMember(
                                TeamMemberDTO(
                                    props.teamMember.id,
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