package pages

import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.router.dom.navLink
import styled.*
import view.ColorSpartak

data class gameNavigation(val header: String, val link: String, val year: String) {}

val gameNavigationList = listOf<gameNavigation>(
    gameNavigation("Первенство СПБ 2003", "/games/championship2003", "2003"),
    gameNavigation("Первенство СПБ 2004", "/games/championship2004", "2004"),
    gameNavigation("Первенство СПБ 2006", "/games/championship2005", "2005")
)

val tableHeaders = listOf<String>("Дата", "Время", "Команда А", "Команда Б", "Стадион", "Результат")

class Games : RComponent<RProps, RState>() {
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
                    navLink<RProps>(to = it.link) {
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

                    }
                }
        }
    }
}