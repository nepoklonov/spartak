package view

import kotlinx.coroutines.CoroutineScope

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import model.Check
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.br
import react.dom.input
import services.CheckService
import styled.StyledDOMBuilder
import styled.styledDiv
import styled.styledForm
import styled.styledInput


external interface AuthFormProps : RProps {
}

class AuthFormState : RState {
    var inputs: MutableMap<String, String> = mutableMapOf("login" to "", "password" to "")
}

class AuthFormComponent : RComponent<AuthFormProps, AuthFormState>() {
    init {
        state = AuthFormState()
    }
    override fun componentDidMount() {
    }
    private fun handleSubmit (event: Event){
        event.preventDefault()
        event.stopPropagation()
        console.log("Login : ${this.state.inputs["login"]} password : ${this.state.inputs["password"]}")

    }

    private fun handleChange(event: Event) {
        console.log(event.target)
        val target = event.target as HTMLInputElement
        setState{
            this.inputs[target.name] = target.value
        }
    }

    override fun RBuilder.render() {
        styledForm {
            styledInput(type = InputType.text) {
                attrs.onChangeFunction = {
                    handleChange(it)
                }
                attrs {
                    this.name = "login"
                    this.value = state.inputs["login"]!!
                }
            }
            styledInput(type = InputType.password) {
                attrs.onChangeFunction = {
                    handleChange(it)
                }

                attrs {
                    this.name = "password"
                    this.value = state.inputs["password"]!!
                }
            }
            styledInput {
                attrs.onSubmitFunction = {
                    handleSubmit(it)
                }
                attrs.type = InputType.submit
                attrs.value = "Submit"
            }
        }
    }
}





