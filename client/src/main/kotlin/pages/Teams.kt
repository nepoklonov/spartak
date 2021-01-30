package pages

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import model.TeamDTO
import model.TeamMemberDTO
import model.TrainerDTO
import react.*
import react.router.dom.navLink
import services.TeamService
import services.TrainerService
import styled.*
import view.ColorSpartak

data class TeamsNavigation(val year: String) {
    val header = "Команда $year"
    val link = "/teams/$year"
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

    private fun getState(selectedTeam: String, coroutineScope: CoroutineScope){
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
        if(this.props.selectedTeam != prevProps.selectedTeam){
            getState(props.selectedTeam, props.coroutineScope)
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                overflow = Overflow.hidden
            }

            styledH1 {
                +"Команды"
            }

            styledDiv {
                css {
                    float = Float.left
                    backgroundColor = Color.white
                    textDecoration = TextDecoration.none
                }
                teamsNavigationList.forEach {
                    navLink<TeamsProps>(to = it.link) {
                        styledDiv {
                            css {
                                textAlign = TextAlign.center
                                color = ColorSpartak.Red.color
                                width = 200.px
                            }
                            styledH2 {
                                css {
                                    margin = 40.px.toString()
                                }
                                +it.header
                            }
                        }
                    }
                }
            }

            val error = state.error
            if (error != null) {
                throw error
            }
            styledDiv {
                styledH2 {
                    +"Тренер"
                }
                if (state.trainer != null) {
                    styledImg(src = "/images/" + state.trainer!!.photo) {
                        css {
                            float = Float.left
                        }
                    }
                    styledH3 {
                        +(state.trainer?.name ?: "загрузка...")
                    }
                    +(state.trainer?.info ?: "загрузка...")
                }
            }

            state.teamMembersWithRoles?.forEach() {
                styledH2 {
                    +it.key
                }
                it.value.forEach {
                    styledImg(src = "/images/" + it.photo) { }
                }
            }
        }
    }
}