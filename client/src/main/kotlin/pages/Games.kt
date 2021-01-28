package pages

import kotlinx.coroutines.CoroutineScope
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import model.GameDTO
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.router.dom.navLink
import styled.*
import view.ColorSpartak

data class GameNavigation(val year: String) {
    val header = "Первенство СПБ $year"
    val link = "/games/championship$year"
}

val gameNavigationList = listOf<GameNavigation>(
    GameNavigation( "2003"),
    GameNavigation("2004"),
    GameNavigation("2006")
)

val tableHeaders = listOf<String>("Дата", "Время", "Команда А", "Команда Б", "Стадион", "Результат")

external interface GamesProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedChampionship: String
}

class GamesState : RState {
    var error: Throwable? = null
    var allGames: List<GameDTO>? = null
}

class Games : RComponent<GamesProps, GamesState>() {
//    private val coroutineContext
//        get() = props.coroutineScope.coroutineContext
//
//    override fun componentDidMount() {
//        val gameService = GameService(coroutineContext)
//
//        props.coroutineScope.launch {
//            val allGamesByYear = try {
//                gameService.getAllGamesByYear(props.selectedChampionship)
//            } catch (e: Throwable) {
//                setState {
//                    error = e
//                }
//                return@launch
//            }
//
//            setState() {
//                allGames = allGamesByYear
//            }
//        }
//    }




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
                                if (props.selectedChampionship == it.header) {
                                    +"урааа"
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

                }
            }
        }
    }
}