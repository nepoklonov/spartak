package pages

import Styles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction
import model.RecruitmentDTO
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.InnerHTML
import services.HtmlService
import services.RecruitmentService
import styled.*
import view.ColorSpartak

external interface RecruitmentProps : RProps {
    var coroutineScope: CoroutineScope
}

data class Input(
    val header: String,
    val inputName: String,
    var inputValue: String = "",
    var isRed: Boolean = false
)

class RecruitmentState : RState {
    var error: Throwable? = null
    var recruitmentHtml: String? = null
    var recruitments: List<RecruitmentDTO>? = null
    var inputs: MutableList<Input> = mutableListOf(
        Input("Предпочтительные даты участия в просмотровых сборах", "dates"),
        Input("Ф.И.О. хоккеиста", "name"),
        Input("Дата рождения(дд.мм.гг.)", "birthday"),
        Input("Амплуа", "role"),
        Input("Хват клюшки", "stickGrip"),
        Input("Рост - Вес", "params"),
        Input("Хоккейная школа в предыдущем сезоне", "previousSchool"),
        Input("Место жительства(город)", "city"),
        Input("Контактный телефон", "phone"),
        Input("E-mail", "email")
    )
}

class Recruitment : RComponent<RecruitmentProps, RecruitmentState>() {
    init {
        state = RecruitmentState()
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    private fun RBuilder.addStyledInput(it: Input, i: Int) {
        styledH3 {
            css {
                if (it.isRed) {
                    color = ColorSpartak.Red.color
                }
            }
            +it.header
        }
        styledInput(type = InputType.text) {
            attrs {
                name = it.inputName
                value = it.inputValue
                onChangeFunction = { event ->
                    val target = event.target as HTMLInputElement
                    setState {
                        inputs[i].inputValue = target.value
                        if (target.value != "") {
                            inputs[i].isRed = false
                        }
                    }
                }
            }
        }
    }

    override fun componentDidMount() {
        val htmlService = HtmlService(coroutineContext)
        val recruitmentService = RecruitmentService(coroutineContext)

        props.coroutineScope.launch {

            val recruitmentHtml = try {
                htmlService.getHtml("htmlPages/Recruitment.html")
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }
            val recruitments = try {
                recruitmentService.getAllRecruitments()
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }
            setState {
                this.recruitmentHtml = recruitmentHtml
                this.recruitments = recruitments
            }
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                textAlign = TextAlign.center
                fontSize = 16.pt
                fontWeight = FontWeight.bold
            }
            if (state.recruitmentHtml != null) {
                css {
                    child("div button") {
                        Styles.button
                        backgroundColor = ColorSpartak.Grey.color
                    }
                }
                attrs["dangerouslySetInnerHTML"] = InnerHTML(state.recruitmentHtml!!)
            } else {
                +"загрузка..."
            }
        }

        styledForm {
            attrs.onSubmitFunction = { event ->
                event.preventDefault()
                event.stopPropagation()
                val recruitmentService = RecruitmentService(coroutineContext)
                props.coroutineScope.launch {
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
                        recruitmentService.addRecruitment(
                            RecruitmentDTO(
                                null,
                                state.inputs[0].inputValue,
                                state.inputs[1].inputValue,
                                state.inputs[2].inputValue,
                                state.inputs[3].inputValue,
                                state.inputs[4].inputValue,
                                state.inputs[5].inputValue,
                                state.inputs[6].inputValue,
                                state.inputs[7].inputValue,
                                state.inputs[8].inputValue,
                                state.inputs[9].inputValue,
                            )
                        )
                    }
                }
            }
            var i = 0
            state.inputs.forEach {
                addStyledInput(it, i)
                i++
            }
            styledInput(type = InputType.submit) {
                attrs.value = "отправить"
            }
        }

        styledDiv {
            if (state.recruitments != null) {
                state.recruitments!!.forEach { dto ->
                    styledDiv {
                        +dto.toString()
                        styledButton {
                            attrs.onClickFunction = {
                                val recruitmentService = RecruitmentService(coroutineContext)
                                props.coroutineScope.launch {
                                    recruitmentService.deleteRecruitment(dto.id!!)
                                }
                            }
                            +"удалить"
                        }
                    }
                }
            }
        }
    }
}