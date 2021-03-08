package pages.teams

import consts.Input
import consts.roleMap
import content
import grid
import header
import isAdmin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import loading
import model.TeamDTO
import model.TeamMemberDTO
import model.TrainerDTO
import navigation
import pageComponents.RedFrameComponent
import react.*
import services.TeamService
import services.TrainerService
import smallHeaderText
import styled.css
import styled.styledDiv

external interface TeamsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedTeam: String
}

class TeamsState : RState {
    var team: TeamDTO? = null
    var trainer: TrainerDTO? = null
    var teamMembersWithRoles: Map<String, List<TeamMemberDTO>>? = null
    var teamInputs: MutableMap<String, Input> = consts.teamInputs
    var trainerInputs: MutableMap<String, Input> = consts.trainerInputs
    var teamMemberInputs: MutableMap<String, Input> = consts.teamMemberInputs
    var addTrainerForm: Boolean = false
    var addTeamMemberForm: Boolean = false
    var editTrainerForm: TrainerDTO? = null
    var editTeamMemberForm: TeamMemberDTO? = null
}

class Teams : RComponent<TeamsProps, TeamsState>() {
    init {
        state = TeamsState()
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    private fun getState(selectedTeam: String, coroutineScope: CoroutineScope) {
        val trainerService = TrainerService(coroutineContext)
        val teamService = TeamService(coroutineContext)

        coroutineScope.launch {
            val team = teamService.getTeamByLink(selectedTeam)

            if (team.id != null) {
                val trainer = if (team.link != null) {
                    trainerService.getTrainerByLink(team.link!!)
                } else {
                    null
                }
                val teamMembersWithRoles = mutableMapOf<String, List<TeamMemberDTO>>()

                roleMap.forEach { role ->
                    val teamMembers = teamService.getTeamMemberByRoleAndTeam(role.key, team.link!!)
                    teamMembersWithRoles[role.value] = teamMembers
                }

                setState {
                    this.team = team
                    this.teamMembersWithRoles = teamMembersWithRoles
                    this.trainer = trainer
                }
            }
        }
    }

    override fun componentDidMount() {
        getState(props.selectedTeam, props.coroutineScope)
    }

    override fun componentDidUpdate(prevProps: TeamsProps, prevState: TeamsState, snapshot: Any) {
        if (this.props.selectedTeam != prevProps.selectedTeam) {
            getState(props.selectedTeam, props.coroutineScope)
        }
    }

    private fun RBuilder.teamMembers(teamMembers: Map.Entry<String, List<TeamMemberDTO>>) {
        smallHeaderText {
            +teamMembers.key
        }
        styledDiv {
            css {
                display = Display.flex
                flexWrap = FlexWrap.wrap
            }

            teamMembers.value.forEach { teamMember ->
                child(RedFrameComponent::class) {
                    attrs.isTrainer = false
                    attrs.teamMember = teamMember
                }
                if (isAdmin) {
                    child(TeamMembersButtons::class) {
                        attrs.coroutineScope = props.coroutineScope
                        attrs.selectedTeam = props.selectedTeam
                        attrs.teamMember = teamMember
                    }
                }
            }
        }
    }

    private fun  RBuilder.trainer(trainer: TrainerDTO){
        styledDiv {
            css {
                display = Display.flex
                justifyContent = JustifyContent.spaceBetween
            }
            child(RedFrameComponent::class) {
                attrs.isTrainer = true
                attrs.trainer = trainer
            }
            styledDiv {
                css {
                    width = 100.pct
                    textAlign = TextAlign.center
                    justifyContent = JustifyContent.center
                    display = Display.flex
                    height = 100.pct
                    padding = 50.px.toString()
                }
                +trainer.info
            }

        }
    }

    override fun RBuilder.render() {
        grid {
            navigation {
                child(TeamsNavigation::class) {
                    attrs.coroutineScope = props.coroutineScope
                    attrs.selectedTeam = props.selectedTeam
                }
            }

            header {
                +"Команды"
            }

            content {
                styledDiv {
                    css {
                        overflow = Overflow.hidden
                    }

                    state.trainer?.let { trainer ->
                        trainer(trainer)
                        if (isAdmin) {
                            child(TrainerButtons::class){
                                attrs.coroutineScope = props.coroutineScope
                                attrs.selectedTeam = props.selectedTeam
                                attrs.trainer = trainer
                            }
                        }
                    } ?: run { loading() }
                }

                state.teamMembersWithRoles?.forEach { teamMembers ->
                    teamMembers(teamMembers)
                } ?: run { loading() }
                if (isAdmin) {
                    child(AddTeamMemberForm::class) {
                        attrs.coroutineScope = props.coroutineScope
                        attrs.selectedTeam = props.selectedTeam
                    }
                }
            }
        }
    }
}