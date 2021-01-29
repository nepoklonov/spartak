package pages

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import model.Check
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


external interface TeamsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedTeam: String
}

class TeamsState : RState {
    var error: Throwable? = null
    var trainer: TrainerDTO? = null
    var check: Check? = null
    var teamMember: List<TeamMemberDTO>? = null

}

class Teams : RComponent<TeamsProps, TeamsState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val trainerService = TrainerService(coroutineContext)
        val teamService = TeamService(coroutineContext)

        props.coroutineScope.launch {
            val trainer = try {
                trainerService.getTrainer("2003")
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }


            val teamMember = try {
                teamService.getTeamMemberByRole("Защитники")
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }

            setState {
                this.teamMember = teamMember
                this.trainer = trainer
            }
        }
    }

    override fun RBuilder.render() {
        styledH1 {
            +"Команды"
        }

        styledDiv {
            css {
                float = kotlinx.css.Float.left
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
        styledH2 {
            +"Игроки"
        }
        state.teamMember?.forEach() {
            styledImg(src = "/images/" + it.photo) { }
        }
    }
}