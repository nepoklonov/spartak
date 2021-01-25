package pages


import kotlinx.browser.document
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.dom.render
import styled.styledDiv
import view.ApplicationProps
import view.ApplicationState
import view.AuthFormComponent

class Admin : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            child(AuthFormComponent::class) {
            }
        }
    }

}