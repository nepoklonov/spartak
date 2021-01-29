package pages

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import model.TeamDTO
import react.*
import react.dom.td
import react.router.dom.navLink
import services.GameService
import services.TeamService
import styled.*
import view.ColorSpartak

data class GameNavigation(val year: String) {
    val header = "Первенство СПБ $year"
    val link = "/games/championship$year"
}

val gameNavigationList = listOf(
    GameNavigation("2003"),
    GameNavigation("2004"),
    GameNavigation("2006")
)

val tableHeaders = listOf("Дата", "Время", "Команда А", "Команда Б", "Стадион", "Результат")

data class GameWhithTeams(
    val date: String,
    val time: String?,
    val teamA: TeamDTO? = null,
    val teamB: TeamDTO? = null,
    val stadium: String,
    val result: String?,
)

external interface GamesProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedChampionship: String
}

class GamesState : RState {
    var error: Throwable? = null
    var allGamesWhithTeams: List<GameWhithTeams>? = null
}

class Games : RComponent<GamesProps, GamesState>() {
    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val gameService = GameService(coroutineContext)
        val teamsService = TeamService(coroutineContext)

        props.coroutineScope.launch {
            val allGames = try {
                gameService.getAllGamesByYear("2003").also { console.log(it) }
            } catch (e: Throwable) {
                console.log(e)
                setState {
                    error = e
                }
                return@launch
            }

            val allGamesWhithTeams: MutableList<GameWhithTeams> = mutableListOf<GameWhithTeams>()

            allGames.forEach {
                val teamA = try {
                    teamsService.getTeamById(it.teamAId)
                } catch (e: Throwable) {
                    console.log(e)
                    setState {
                        error = e
                    }
                    return@launch
                }
                val teamB = try {
                    teamsService.getTeamById(it.teamBId)
                } catch (e: Throwable) {
                    console.log(e)
                    setState {
                        error = e
                    }
                    return@launch
                }

                allGamesWhithTeams += GameWhithTeams(it.date, it.time, teamA, teamB, it.stadium, it.result)
            }

            setState() {
                this.allGamesWhithTeams = allGamesWhithTeams
            }
        }
    }


    override fun RBuilder.render() {
        styledH1 {
            css {
                textAlign = TextAlign.center
            }
            +"Расписание игр"
        }
        styledDiv {
            css {
                overflow = Overflow.hidden
                margin = 40.px.toString()
            }

            styledDiv {
                css {
                    float = Float.left
                    backgroundColor = Color.white
                    textDecoration = TextDecoration.none
                }
                gameNavigationList.forEach {
                    navLink<GamesProps>(to = it.link) {
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
            styledTable {
                css {
                    textAlign = TextAlign.center
                    width = 1000.px
                }
                styledThead {
                    css {
                        backgroundColor = ColorSpartak.Grey.color
                    }
                    tableHeaders.forEach {
                        styledTh() {
                            +it
                        }
                    }
                }
                styledTbody {
                    css {
                        backgroundColor = Color.white
                    }
                    state.allGamesWhithTeams?.forEach {
                        td {
                            +it.date
                        }
                        td {
                            +it.time!!
                        }
                        td {
                            +it.teamA?.name!!
                        }
                        td {
                            +it.teamB?.name!!
                        }
                        td {
                            +it.stadium
                        }
                        td {
                            +it.result!!
                        }
                    }
                }
            }
        }
    }
}