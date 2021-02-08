package pageComponents

import kotlinx.html.js.onSubmitFunction
import react.*
import styled.styledForm

external interface SmallNavigationFormProps : RProps {
    var addSection: (List<String>) -> Unit
    var isTeam: Boolean
}

class SmallNavigationFormState : RState {
    val inputs: MutableList<Input> = mutableListOf<Input>(
        Input("Название раздела", "sectionName"),
        Input("Ссылка", "sectionLink")
    )
}

class SmallNavigationForm : RComponent<SmallNavigationFormProps, SmallNavigationFormState>() {
    init {
        state = SmallNavigationFormState()
    }

    override fun RBuilder.render() {
        styledForm {
            attrs.onSubmitFunction = { event ->
                event.preventDefault()
                event.stopPropagation()
                var formIsCompleted = true
                state.inputs.forEach {
                    if (it.inputValue == "") {
                        setState {
                            it.isRed = true
                        }
                        formIsCompleted = false
                    }
                }
                if (formIsCompleted) {
                    val listOfInputValues = mutableListOf<String>()
                    state.inputs.forEach {
                        listOfInputValues += it.inputValue
                    }
                    props.addSection(listOfInputValues)
                }
            }
            child(FormViewComponent::class) {
                attrs.inputs = state.inputs
                attrs.updateState = { i: Int, value: String, isRed: Boolean ->
                    setState {
                        state.inputs[i].inputValue = value
                        state.inputs[i].isRed = isRed
                    }
                }
            }
        }
    }
}