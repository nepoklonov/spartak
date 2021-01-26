package view

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import styled.styledForm
import styled.styledInput

external interface AuthFormProps : RProps

class AuthFormState : RState {
    var inputs: MutableMap<String, String> = mutableMapOf("login" to "", "password" to "")
}

class AuthFormComponent : RComponent<AuthFormProps, AuthFormState>() {
    init {
        state = AuthFormState()
    }

    private fun handleSubmit(event: Event) {
        event.preventDefault()
        console.log("Login : ${state.inputs["login"]} password : ${state.inputs["password"]}")
    }

    private fun handleChange(event: Event) {
        val target = event.target as HTMLInputElement
        setState {
            inputs[target.name] = target.value
        }
    }

    override fun RBuilder.render() {
        styledForm {
            attrs.onSubmitFunction = {
                handleSubmit(it)
            }
            styledInput(type = InputType.text) {
                attrs.onChangeFunction = {
                    handleChange(it)
                }
                attrs {
                    name = "login"
                    value = state.inputs["login"]!!
                }
            }
            styledInput(type = InputType.password) {
                attrs.onChangeFunction = {
                    handleChange(it)
                }

                attrs {
                    name = "password"
                    value = state.inputs["password"]!!
                }
            }
            styledInput {
                attrs.type = InputType.submit
                attrs.value = "Submit"
            }
        }
    }
}





