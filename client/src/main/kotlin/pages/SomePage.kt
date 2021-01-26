package pages

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.styledDiv

class SomePage : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            +"ROUTING IS WORKING"
        }
    }
}

