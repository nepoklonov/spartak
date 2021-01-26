package pages

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.styledDiv
import view.AuthFormComponent

class Admin : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            child(AuthFormComponent::class) { }
        }
    }
}