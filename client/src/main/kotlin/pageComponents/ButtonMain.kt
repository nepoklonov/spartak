package pageComponents

import redButtonSpartak
import kotlinx.css.*
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css

external interface ButtonProps : RProps {
    var text: String
}
class ButtonMain : RComponent<ButtonProps, RState>() {

    override fun RBuilder.render() {
        redButtonSpartak{
            + props.text
            css{
                backgroundColor = Color("#9D0707")
                hover {
                    backgroundColor = Color("#660c0c")
                }
            }
        }
    }

}

fun RBuilder.buttonMain(text:String) {
    redButtonSpartak {
        + text
        css{
            backgroundColor = Color("#9D0707")
            hover {
                backgroundColor = Color("#660c0c")
            }
        }
    }
}