package structure

import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import kotlinx.css.properties.boxShadow
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.h2
import react.router.dom.navLink
import styled.css
import styled.styledDiv

enum class Pages(val header: String, val link: String) {
    Main("Главная", "/main"),
    News("Новости", "/news"),
    Games("Игры", "/games"),
    Teams("Команды", "/teams"),
    Recruitment("Набор", "/recruitment"),
    Workouts("Тренировки", "/workouts"),
    SummerCamp("Летние Сборы", "/summerCamp"),
    Gallery("Галерея", "/gallery")
}

external interface MainNavigationProps : RProps {
    var selectedString: String
}


class MainNavigationComponent : RComponent<MainNavigationProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                height = 70.px
                backgroundColor = ColorSpartak.Red.color
                display = Display.flex
                justifyContent = JustifyContent.spaceAround
                textAlign = TextAlign.center
                boxShadow(color = rgba(0, 0, 0, 0.25), offsetX = 0.px, offsetY = 4.px, blurRadius = 4.px)
            }

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
                        if ("/${props.selectedString}(/.*)?".toRegex().matches(page.link)) {
                            borderBottomColor = Color.white
                            borderBottomWidth = 5.px
                            borderBottomStyle = BorderStyle.solid
                        }
                    }
                    navLink<RProps>(to = page.link) {
                        h2 {
                            +page.header
                        }
                    }
                }
            }
        }
    }
}
