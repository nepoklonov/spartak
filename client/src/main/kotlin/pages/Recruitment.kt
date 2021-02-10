package pages

import Styles
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction
import model.RecruitmentDTO
import pageComponents.ColorSpartak
import pageComponents.FormViewComponent
import pageComponents.Input
import react.*
import react.dom.InnerHTML
import services.HtmlService
import services.RecruitmentService
import styled.*

external interface RecruitmentProps : RProps {
    var coroutineScope: CoroutineScope
}

class RecruitmentState : RState {
    var error: Throwable? = null
    var recruitmentHtml: String? = null
    var recruitments: List<RecruitmentDTO>? = null
    var inputs: MutableMap<String, Input> = mutableMapOf(
        "dates" to Input("Предпочтительные даты участия в просмотровых сборах", "dates"),
        "name" to Input("Ф.И.О. хоккеиста", "name"),
        "birthday" to Input("Дата рождения(дд.мм.гг.)", "birthday"),
        "role" to Input(
            "Амплуа",
            "role",
            isSelect = true,
            options = mapOf("Защитник" to "Защитник", "Вратарь" to "Вратарь", "Нападающий" to "Нападающий")
        ),
        "stickGrip" to Input("Хват клюшки", "stickGrip"),
        "params" to Input("Рост - Вес", "params"),
        "previousSchool" to Input("Хоккейная школа в предыдущем сезоне", "previousSchool"),
        "city" to Input("Место жительства(город)", "city"),
        "phone" to Input("Контактный телефон", "phone"),
        "email" to Input("E-mail", "email")
    )
}

class Recruitment : RComponent<RecruitmentProps, RecruitmentState>() {
    init {
        state = RecruitmentState()
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

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
                    state.inputs.values.forEach {
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
                                state.inputs["dates"]!!.inputValue,
                                state.inputs["name"]!!.inputValue,
                                state.inputs["birthday"]!!.inputValue,
                                state.inputs["role"]!!.inputValue,
                                state.inputs["stickGrip"]!!.inputValue,
                                state.inputs["params"]!!.inputValue,
                                state.inputs["previousSchool"]!!.inputValue,
                                state.inputs["city"]!!.inputValue,
                                state.inputs["phone"]!!.inputValue,
                                state.inputs["email"]!!.inputValue,
                            )
                        )
                    }
                }
            }
            child(FormViewComponent::class) {
                attrs.inputs = state.inputs
                attrs.updateState = { key: String, value: String, isRed: Boolean ->
                    setState {
                        state.inputs[key]!!.inputValue = value
                        state.inputs[key]!!.isRed = isRed
                    }
                }
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