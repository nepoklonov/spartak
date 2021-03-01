package adminPageComponents

import kotlinx.html.js.onSubmitFunction
import react.*
import styled.styledForm

external interface SmallNavigationFormProps : RProps {
    var inputValues: List<String>
    var onSubmitFunction: (List<String>) -> Unit
}

class SmallNavigationFormState : RState {
    val inputs: MutableMap<String, Input> = mutableMapOf(
        "sectionName" to Input("Название раздела", "sectionName"),
        "sectionLink" to Input("Ссылка", "sectionLink")
    )
}

class SmallNavigationForm : RComponent<SmallNavigationFormProps, SmallNavigationFormState>() {
    init {
        state = SmallNavigationFormState()
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
            child(FormComponent::class) {
                attrs.inputs = state.inputs
                attrs.updateState = { key: String, value: String, isRed: Boolean ->
                    setState {
                        state.inputs[key]!!.inputValue = value
                        state.inputs[key]!!.isRed = isRed
                    }
                }
            }
        }
    }
}