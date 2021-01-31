package view

import kotlinx.browser.window
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.router.dom.navLink
import styled.css
import styled.styledDiv

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

external interface MainNavigationProps : RProps {
    var selectedString: String
}


class MainNavigationComponent : RComponent<MainNavigationProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                height = 50.px
                backgroundColor = ColorSpartak.Red.color
                display = Display.flex
                justifyContent = JustifyContent.spaceAround
                textAlign = TextAlign.center
            }

//            +props.selectedString

            Pages.values().forEach { page ->
                styledDiv {
                    css {
                        fontFamily = "Russo"
                        fontSize = 11.pt
                        float = Float.left
                        display = Display.flex
                        alignItems = Align.center
                        child("a") {
                            textDecoration = TextDecoration.none
                            color = Color.white
                        }
                        console.log(page.link)
                        console.log(props.selectedString)
                        console.log("${page.link}(/.*)?")
                        console.log(33333)
                        if ("/${props.selectedString}(/.*)?".toRegex().matches(page.link)){
                            borderBottomColor = Color.white
                            borderBottomWidth = 3.px
                            borderBottomStyle = BorderStyle.solid
                        }
                    }
                    navLink<RProps>(to = page.link) {
                        +page.header
                    }
                }
            }
        }
    }
}
