package pages

import headerText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import model.TeamDTO
import model.TeamMemberDTO
import model.TrainerDTO
import react.*
import react.router.dom.route
import services.TeamService
import services.TrainerService
import smallHeaderText
import styled.*
import view.SmallNavigation
import view.SmallNavigationProps

data class TeamsNavigation(val year: String) {
    val header = "Команда $year"
    val link = year
}

val teamsNavigationList = listOf(
    TeamsNavigation("2003"),
    TeamsNavigation("2004"),
    TeamsNavigation("2006")
)

val roleList = listOf("Защитники", "Вратари", "Нападающие")


external interface TeamsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedTeam: String
}

class TeamsState : RState {
    var error: Throwable? = null
    var team: TeamDTO? = null
    var trainer: TrainerDTO? = null
    var teamMembersWithRoles: Map<String, List<TeamMemberDTO>>? = null

}

class Teams : RComponent<TeamsProps, TeamsState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    private fun getState(selectedTeam: String, coroutineScope: CoroutineScope) {
        val trainerService = TrainerService(coroutineContext)
        val teamService = TeamService(coroutineContext)

        coroutineScope.launch {
            val team = try {
                teamService.getTeamByYear(selectedTeam)
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }
            if (team.id != null) {
                val trainer = try {
                    trainerService.getTrainerById(team.id!!)
                } catch (e: Throwable) {
                    setState {
                        error = e
                    }
                    return@launch
                }

                val teamMembersWithRoles = mutableMapOf<String, List<TeamMemberDTO>>()

                roleList.forEach { header ->
                    val teamMembers = try {
                        teamService.getTeamMemberByTeamIdAndRole(header, team.id!!)
                    } catch (e: Throwable) {
                        setState {
                            error = e
                        }
                        return@launch
                    }
                    teamMembersWithRoles[header] = teamMembers

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

    override fun RBuilder.render() {
        styledDiv {
            css {
                overflow = Overflow.hidden
            }

            headerText {
                +"Команды"
            }

            styledDiv {
                css {
                    float = Float.left
                    backgroundColor = Color.white
                    textDecoration = TextDecoration.none
                }
                teamsNavigationList.forEach { teamsNavigationProps ->
                    route<SmallNavigationProps>("/teams/:selectedLink") { linkProps ->
                        child(SmallNavigation::class) {
                            attrs.string = teamsNavigationProps.header
                            attrs.link = teamsNavigationProps.link
                            attrs.selectedLink = linkProps.match.params.selectedLink
                        }
                    }
                }
            }

            val error = state.error
            if (error != null) {
                throw error
            }
            styledDiv {
                css {
                    overflow = Overflow.hidden
                }
                smallHeaderText {
                    +"Тренер"
                }
                if (state.trainer != null) {
                    styledImg(src = "/images/" + state.trainer!!.photo) {
                        css {
                            float = Float.left
                        }
                    }
                    styledDiv {
                        css {
                            textAlign = TextAlign.center
                        }
                        smallHeaderText {
                            +(state.trainer?.name ?: "загрузка...")
                        }
                        +(state.trainer?.info ?: "загрузка...")
                    }

                }
            }
            if (state.teamMembersWithRoles != null) {
                state.teamMembersWithRoles!!.forEach() {
                    smallHeaderText {
                        +it.key
                    }
                    it.value.forEach {
                        styledImg(src = "/images/" + it.photo) { }
                    }
                }
            }
        }
    }
}