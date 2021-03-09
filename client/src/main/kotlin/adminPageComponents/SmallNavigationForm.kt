package adminPageComponents

import consts.Input
import consts.navigationInputs
import kotlinx.html.js.onSubmitFunction
import org.w3c.files.File
import pageComponents.FormState
import pageComponents.formComponent
import react.RBuilder
import react.RComponent
import react.RProps
import react.setState
import styled.styledForm

external interface SmallNavigationFormProps : RProps {
    var inputValues: List<String>
    var onSubmitFunction: (List<String>) -> Unit
}

class SmallNavigationFormState : FormState {
    override var inputs: MutableMap<String, Input> = navigationInputs
    override var file: File? = null
}

class SmallNavigationForm : RComponent<SmallNavigationFormProps, SmallNavigationFormState>() {
    init {
        state.inputs = navigationInputs
    }

    override fun componentDidMount(){
        setState{
            inputs["sectionName"]!!.inputValue = props.inputValues[0]
            inputs["sectionLink"]!!.inputValue = props.inputValues[1]
        }
    }

    override fun RBuilder.render() {
        styledForm {
            attrs.onSubmitFunction = { event ->
                event.preventDefault()
                event.stopPropagation()
                var formIsCompleted = true
                state.inputs.values.forEach {
                    if (it.inputValue == "") {
                        setState {
                            it.isRed = true
                        }
                        formIsCompleted = false
                    }
                }
                if (formIsCompleted) {
                    val listOfInputValues = mutableListOf<String>()
                    state.inputs.values.forEach {
                        listOfInputValues += it.inputValue
                    }
                    props.onSubmitFunction(listOfInputValues)
                }
            }
            formComponent(this)
        }
    }
}