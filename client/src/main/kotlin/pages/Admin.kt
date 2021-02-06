package pages

import kotlinx.coroutines.CoroutineScope
import pageComponents.AuthFormComponent
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.styledDiv

interface AdminProps : RProps {
    var coroutineScope: CoroutineScope
}

class Admin : RComponent<AdminProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            child(AuthFormComponent::class) {
                attrs.coroutineScope = props.coroutineScope
            }
        }
    }
}