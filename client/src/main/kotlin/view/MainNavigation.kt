package view

import kotlinx.css.*
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.router.dom.navLink
import styled.css
import styled.styledDiv
import styled.styledH1

enum class Pages(val header: String, val link: String) {
    Main("Главная", "/main"),
    News("Новости", "/news/feed"),
    Games("Игры", "/games/championship2003"),
    Club("Команды", "/teams/2003"),
    Recruitment("Набор", "/recruitment"),
    Workouts("Тренировки", "/workouts/shhm"),
    SummerCamp("Летние Сборы", "/summerCamp"),
    Gallery("Галерея", "/gallery/trainingProcess")
}


class MainNavigationComponent : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                height = 50.px
                backgroundColor = ColorSpartak.Red.color
                display = Display.flex
                justifyContent = JustifyContent.spaceAround
            }

            Pages.values().forEach { page ->

                navLink<RProps>(to = page.link) {
                    styledH1 {
                        css {
                            color = Color.white
                            fontSize = 14.pt
                            float = Float.left
                        }
                        +page.header
                    }
                }
            }
        }
    }
}
