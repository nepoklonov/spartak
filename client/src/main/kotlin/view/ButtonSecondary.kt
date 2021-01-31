package view

import redButtonSpartak
import kotlinx.css.*
import react.RBuilder
import react.RComponent
import react.RState
import styled.css


class ButtonSecondary : RComponent<ButtonProps, RState>() {

    override fun RBuilder.render() {
        redButtonSpartak {
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