package view

import kotlinx.css.*
import react.RBuilder
import react.RProps
import react.RState
import styled.css
import styled.styledDiv
import styled.styledH1

enum class Pages(val header : String, val link: String){
    Main("Главная", ""),
    News("Новости",""),
    Games("Игры",""),
    Club("Клуб",""),
    Recruitment("Набор",""),
    Workouts("Тренировки",""),
    SummerCamp("Летние Сборы", ""),
    Gallery("Галерея", "")
}


class NavigationComponent : RootComponent<RProps, RState>() {
    override fun RBuilder.render(){
        styledDiv{
            css {
                height = 50.px
                backgroundColor = Color("#9D0707")
                display = Display.flex
                justifyContent = JustifyContent.spaceAround
            }
            Pages.values().forEach { page ->
                styledH1 {
                    css{
                        color = Color.white
                        fontSize = 14.pt
                        float = Float.left
                    }
                    + page.header
                }
            }

        }
    }
}