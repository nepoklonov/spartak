package pages

import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.html.js.onClickFunction
import pageComponents.AuthFormComponent
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.styledButton
import styled.styledDiv

interface AdminProps : RProps {
    var coroutineScope: CoroutineScope
}

class Admin : RComponent<AdminProps, RState>() {
    override fun RBuilder.render() {
        console.log(document.cookie)
        styledDiv {
            child(AuthFormComponent::class) {
                attrs.coroutineScope = props.coroutineScope
            }
        }
        styledButton {
            attrs.onClickFunction = {
                document.cookie = "role=admin"
                console.log(document.cookie)
            }
            +"войти"
        }
        styledButton {
            attrs.onClickFunction = {
                document.cookie = "role=basic"
                console.log(document.cookie)
            }
            +"выйти"
        }
    }
}