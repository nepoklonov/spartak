package pageComponents

import kotlinx.css.color
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import react.*
import styled.*

data class Input(
    val header: String,
    val inputName: String,
    var inputValue: String = "",
    var isRed: Boolean = false,
    val isSelect: Boolean = false,
    val options: Map<String, String> = mapOf(),
    val allowOtherOption: Boolean = false,
    var otherOption: Boolean = false
)

external interface FormViewComponentProps : RProps {
    var inputs: MutableList<Input>
    var updateState: (Int, String, Boolean) -> Unit
}

class FormViewComponentState : RState{
    var otherOption: Boolean = false
}

class FormViewComponent : RComponent<FormViewComponentProps, FormViewComponentState>() {
    private fun RBuilder.addStyledInput( it: Input, i: Int) {
        styledH3 {
            css {
                if (it.isRed) {
                    color = ColorSpartak.Red.color
                }
            }
            +it.header
        }
        if(it.isSelect){
            styledSelect {
                attrs {
                    name = it.inputName
                    onChangeFunction = { event ->
                        val target = event.target as HTMLSelectElement
                        var isRed = false
                        if (target.value == "nothing") {
                            isRed = true
                        }
                        if (target.value == ""){
                            it.otherOption = true
                            setState {
                                otherOption = true
                            }
                        }
                        props.updateState(i, target.value, isRed)
                    }
                }
                styledOption {
                    attrs{
                        value = "nothing"
                    }
                    + it.header
                }
                it.options.forEach {
                    styledOption {
                        attrs{
                            value = it.key
                        }
                        + it.value
                    }
                }
                if (it.allowOtherOption){
                    styledOption {
                        attrs {
                            value = ""
                        }
                        +"Другая команда"
                    }
                }
            }
            if(it.otherOption){
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
                            props.updateState(i, target.value, isRed)
                        }
                    }
                }
            }
        }else{
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
                        props.updateState(i, target.value, isRed)
                    }
                }
            }
        }
    }

    override fun RBuilder.render() {
        var i = 0
        props.inputs.forEach {
            addStyledInput(it, i)
            i++
        }
        styledInput(type = InputType.submit) {
            attrs.value = "отправить"
        }
    }
}