package pages

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import styled.styledDiv
import styled.styledForm
import styled.styledInput
import view.ApplicationComponent
import view.AuthFormComponent
import view.AuthFormProps
import view.AuthFormState


class Main : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            child(ApplicationComponent::class) {
            }
        }
    }

}
