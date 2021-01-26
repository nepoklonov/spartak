package pages

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.styledDiv
import view.ApplicationComponent

class Main : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            child(ApplicationComponent::class) {}
        }
    }

}
