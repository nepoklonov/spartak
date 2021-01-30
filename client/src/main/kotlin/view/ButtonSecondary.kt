package view

import buttonSpartak
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


class ButtonSecondary : RComponent<ButtonProps, RState>() {

    override fun RBuilder.render() {
        buttonSpartak {
            + props.text
            css{
                backgroundColor = Color("#484444")
                hover {
                    backgroundColor = Color("#382828")
                }
            }
        }
    }

}