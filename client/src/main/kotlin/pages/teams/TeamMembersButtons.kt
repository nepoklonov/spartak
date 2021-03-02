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

external interface TeamMembersButtonsProps : RProps {
    var coroutineScope: CoroutineScope
    var teamMember: TeamMemberDTO
    var selectedTeam: String
}

class TeamMembersButtonsState : RState {
    var teamMemberInputs: MutableMap<String, Input> = Consts.teamMemberInputs
    var editTeamMemberForm: TeamMemberDTO? = null
}

class TeamMembersButtons: RComponent<TeamMembersButtonsProps, TeamMembersButtonsState>() {
    init{
        state = TeamMembersButtonsState()
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
                    teamMemberInputs["number"]!!.inputValue = props.teamMember.number
                    teamMemberInputs["firstName"]!!.inputValue = props.teamMember.firstName
                    teamMemberInputs["lastName"]!!.inputValue = props.teamMember.lastName
                    teamMemberInputs["role"]!!.inputValue = props.teamMember.role
                    teamMemberInputs["birthday"]!!.inputValue = props.teamMember.birthday
                    teamMemberInputs["city"]!!.inputValue = props.teamMember.city
                    teamMemberInputs["teamRole"]!!.inputValue = props.teamMember.number
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
                            teamService.editTeamMember(
                                TeamMemberDTO(
                                    props.teamMember.id,
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