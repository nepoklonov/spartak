package pageComponents

import kotlinx.css.*
import react.RBuilder
import styled.css
import styled.styledDiv

fun RBuilder.background(){
    styledDiv {
        css {
            minWidth = 1260.px
            position = Position.fixed
            backgroundImage = Image("url(/images/background.jpg)")
            backgroundSize = "cover"
            opacity = 0.8
            width = 100.pct
            height = 100.pct
            child("div") {
                fontFamily = "PT"
            }
        }
    }
}