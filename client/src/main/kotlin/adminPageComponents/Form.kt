package adminPageComponents

import kotlinx.css.color
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import pageComponents.buttonMain
import react.*
import styled.*

data class Input(
    val header: String,
    val inputName: String,
    var inputValue: String = "",
    var isRed: Boolean = false,
    val isSelect: Boolean = false,
    var options: Map<String, String> = mapOf(),
    val allowOtherOption: Boolean = false,
    var otherOption: Boolean = false,
    val isDateTime: Boolean = false,
    val isNecessary: Boolean = true
)

external interface FormViewComponentProps : RProps {
    var inputs: MutableMap<String, Input>
    var updateState: (String, String, Boolean) -> Unit
    var addOtherOption: (Boolean, String) -> Unit
}

class FormViewComponentState : RState {
    var otherOption: Boolean = false
}

class FormComponent : RComponent<FormViewComponentProps, FormViewComponentState>() {
    private fun RBuilder.selectInput(it: Input, key: String){
        styledSelect {
            attrs {
                name = it.inputName
                onChangeFunction = { event ->
                    val target = event.target as HTMLSelectElement
                    var isRed = false
                    if (target.value == "nothing") {
                        isRed = true
                    }
                    if (target.value == "") {
                        it.otherOption = true
                        setState {
                            otherOption = true
                        }
                    }
                    props.updateState(key, target.value, isRed)
                }
            }
            styledOption {
                attrs {
                    value = "nothing"
                }
                +it.header
            }
            it.options.forEach {
                styledOption {
                    attrs {
                        value = it.key
                    }
                    +it.value
                }
            }
            if (it.allowOtherOption) {
                styledOption {
                    attrs {
                        value = ""
                    }
                    +"Другая команда"
                }
            }
        }
        if (it.otherOption) {
            styledH3 {
                +"Название команды"
            }
            styledInput(type = InputType.text) {
                attrs {
                    name = it.inputName
                    value = it.inputValue
                    onChangeFunction = { event ->
                        val target = event.target as HTMLInputElement
                        var isRed = false
                        if (target.value == "") {
                            isRed = true
                        }
                        val isItTeamA = (it.inputName == "teamA")
                        props.updateState(key, target.value, isRed)
                        props.addOtherOption(isItTeamA, target.value)
                    }
                }
            }
        }
    }

    private fun RBuilder.datetimeOrTextInput(it: Input, type: InputType, key: String){
        styledInput(type = type) {
            attrs {
                name = it.inputName
                value = it.inputValue
                onChangeFunction = { event ->
                    val target = event.target as HTMLInputElement
                    var isRed = false
                    if (target.value == "") {
                        isRed = true
                    }
                    props.updateState(key, target.value, isRed)
                }
            }
        }
    }

    private fun RBuilder.addStyledInput(it: Input, key: String) {
        styledDiv {
            styledH3 {
                css {
                    if (it.isRed) {
                        color = ColorSpartak.Red.color
                    }
                }
                +it.header
            }
            when {
                it.isSelect -> {
                    selectInput(it, key)
                }
                it.isDateTime -> {
                    datetimeOrTextInput(it, InputType.dateTimeLocal, key)
                }
                else -> {
                    datetimeOrTextInput(it, InputType.text, key)
                }
            }
        }
    }

    override fun RBuilder.render() {
        props.inputs.forEach {
            addStyledInput(it.value, it.key)
        }
        buttonMain("Отправить")
    }
}
