package pageComponents

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import services.AdminService
import styled.styledForm
import styled.styledInput


external interface AuthFormProps : RProps {
    var coroutineScope: CoroutineScope
}

class AuthFormState : RState {
    var inputs: MutableMap<String, String> = mutableMapOf("login" to "", "password" to "")
}

class AuthFormComponent : RComponent<AuthFormProps, AuthFormState>() {
    init {
        state = AuthFormState()
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    private fun handleSubmit(adminService: AdminService, event: Event) {
        event.preventDefault()
        event.stopPropagation()
        console.log("Login : ${state.inputs["login"]} password : ${state.inputs["password"]}")
        props.coroutineScope.launch {
            if (adminService.checkAdmin(state.inputs["login"].toString(), state.inputs["password"].toString())) {
                console.log("LOGGED IN")
            } else {
                console.log("FAILED")
            }
        }
    }

    private fun handleChange(event: Event) {
        val target = event.target as HTMLInputElement
        setState {
            inputs[target.name] = target.value
        }
    }

    override fun RBuilder.render() {
        val adminService = AdminService(coroutineContext)
        styledForm {
            attrs.onSubmitFunction = {
                handleSubmit(adminService, it)
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





