package pages

import headerText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import kotlinx.html.js.onSubmitFunction
import model.GameDTO
import model.TeamDTO
import pageComponents.FormViewComponent
import pageComponents.Input
import pageComponents.SmallNavigation
import pageComponents.SmallNavigationProps
import react.*
import react.dom.td
import react.dom.tr
import react.router.dom.route
import services.GameService
import services.TeamService
import styled.*
import tableHeader

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
    var inputs: MutableList<Input> = mutableListOf(
        Input("Дата", "date"),
        Input("Время", "time"),
        Input(
            "Команда А",
            "teamAId",
            isSelect = true,
            options = mapOf("1" to "Команда 2003", "2" to "Команда 2004"),
            allowOtherOption = true
        ),
        Input(
            "Команда Б",
            "teamBId",
            isSelect = true,
            options = mapOf("1" to "Команда 2003", "2" to "Команда 2004"),
            allowOtherOption = true
        ),
        Input("Стадион", "stadium"),
        Input("Результат", "result"),
    )
}

class Games : RComponent<GamesProps, GamesState>() {
    init {
        state = GamesState()
    }

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
        styledForm {
            attrs.onSubmitFunction = { event ->
                event.preventDefault()
                event.stopPropagation()
                val gameService = GameService(coroutineContext)
                props.coroutineScope.launch {
                    var formIsCompleted = true
                    state.inputs.forEach {
                        if (it.inputValue == "") {
                            setState {
                                it.isRed = true
                            }
                            formIsCompleted = false
                        }
                    }
                    if (formIsCompleted) {
                        gameService.addGame(
                            GameDTO(
                                null,
                                state.inputs[0].inputValue,
                                state.inputs[1].inputValue,
                                props.selectedChampionship,
                                state.inputs[2].inputValue.toInt(),
                                state.inputs[3].inputValue.toInt(),
                                state.inputs[4].inputValue,
                                state.inputs[5].inputValue,
                            )
                        )
                    }
                }
            }
            child(FormViewComponent::class) {
                attrs.inputs = state.inputs
                attrs.updateState = { i: Int, value: String, isRed: Boolean ->
                    setState {
                        state.inputs[i].inputValue = value
                        state.inputs[i].isRed = isRed
                    }
                }
            }
        }
    }
}