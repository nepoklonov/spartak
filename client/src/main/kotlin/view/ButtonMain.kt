package view

import buttonSpartak
import kotlinx.coroutines.CoroutineScope
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.a
import styled.css
import styled.styledButton
import styled.styledImg

external interface ButtonProps : RProps {
    var text: String
}
class ButtonMain : RComponent<ButtonProps, RState>() {

    override fun RBuilder.render() {
        buttonSpartak{
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