package pages

import headerText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import model.TeamDTO
import react.*
import react.dom.td
import react.dom.tr
import react.router.dom.route
import services.GameService
import services.TeamService
import styled.*
import tableHeader
import view.SmallNavigation
import view.SmallNavigationProps

data class GameNavigation(val year: String) {
    val header = "Первенство СПБ $year"
    val link = "championship$year"
}

val gameNavigationList = listOf(
    GameNavigation("2003"),
    GameNavigation("2004"),
    GameNavigation("2006")
)

val tableHeaders = listOf("Дата", "Время", "Команда А", "Команда Б", "Стадион", "Результат")

data class GameWithTeams(
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
    var allGamesWithTeams: List<GameWithTeams>? = null
}

class Games : RComponent<GamesProps, GamesState>() {
    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    private fun getState(year: String, coroutineScope: CoroutineScope) {
        val gameService = GameService(coroutineContext)
        val teamsService = TeamService(coroutineContext)

        coroutineScope.launch {
            val allGames = try {
                gameService.getAllGamesByYear(year)
            } catch (e: Throwable) {
                console.log(e)
                setState {
                    error = e
                }
                return@launch
            }

            val allGamesWithTeams: MutableList<GameWithTeams> = mutableListOf<GameWithTeams>()

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

                allGamesWithTeams += GameWithTeams(it.date, it.time, teamA, teamB, it.stadium, it.result)
            }

            setState() {
                this.allGamesWithTeams = allGamesWithTeams
            }
        }
    }

    override fun componentDidMount() {
        getState(props.selectedChampionship, props.coroutineScope)
    }

    override fun componentDidUpdate(prevProps: GamesProps, prevState: GamesState, snapshot: Any) {
        if (this.props.selectedChampionship != prevProps.selectedChampionship) {
            getState(props.selectedChampionship, props.coroutineScope)
        }
    }


    override fun RBuilder.render() {
        headerText {
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
                gameNavigationList.forEach { gameNavigationProps ->
                    route<SmallNavigationProps>("/games/:selectedLink") { linkProps ->
                        child(SmallNavigation::class) {
                            attrs.string = gameNavigationProps.header
                            attrs.link = gameNavigationProps.link
                            attrs.selectedLink = linkProps.match.params.selectedLink
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
                    tableHeader {
                        tableHeaders.forEach {
                            styledTh() {
                                +it
                            }
                        }
                    }
                }
                styledTbody {
                    css {
                        backgroundColor = Color.white
                    }

                    state.allGamesWithTeams?.forEach {
                        tr {
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
}