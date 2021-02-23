package pageComponents

import kotlinx.css.Color
import kotlinx.css.backgroundColor
import react.RBuilder
import redButtonSpartak
import styled.css

fun RBuilder.buttonMain(text: String) {
    redButtonSpartak {
        +text
        css {
            backgroundColor = Color("#9D0707")
            hover {
                backgroundColor = Color("#660c0c")
            }
        }
    }
}

fun RBuilder.buttonSecondary(text: String) {
    redButtonSpartak {
        +text
        css {
            backgroundColor = Color("#484444")
            hover {
                backgroundColor = Color("#382828")
            }
        }
    }
}