package view

import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css
import styled.styledDiv
import styled.styledH2

external interface SmallNavigationProps : RProps {
    var selectedString: String
}

class SmallNavigation : RComponent<SmallNavigationProps, RState>() {
    override fun RBuilder.render() {
        styledDiv{
            css {
                fontFamily = "Russo"
                textAlign = TextAlign.center
                color = ColorSpartak.Red.color
                width = 250.px
            }
            styledH2 {
                css {
                    margin = 40.px.toString()
                    textDecoration = TextDecoration.none
                }
                +props.selectedString
            }
        }
    }
}