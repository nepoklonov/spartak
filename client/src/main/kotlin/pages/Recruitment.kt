package pages

import Consts.recruitmentInputs
import adminPageComponents.*
import isAdmin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.js.onSubmitFunction
import loading
import model.RecruitmentDTO
import pageComponents.*
import react.*
import react.dom.InnerHTML
import services.HtmlService
import services.RecruitmentService
import styled.*

external interface RecruitmentProps : RProps {
    var coroutineScope: CoroutineScope
}

interface FormState : RState {
    var inputs: MutableMap<String, Input>
}

class RecruitmentState : FormState {
    var error: Throwable? = null
    var recruitmentHtml: String? = null
    var recruitments: List<RecruitmentDTO>? = null
    override var inputs = recruitmentInputs
}

class Recruitment : RComponent<RecruitmentProps, RecruitmentState>() {
    init {
        state.error = null
        state.recruitmentHtml = null
        state.recruitments = null
        state.inputs = recruitmentInputs
            //TODO: понять, почему так не работает
//        state = RecruitmentState()
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
                display = Display.flex
                justifyContent = JustifyContent.center
                flexWrap = FlexWrap.wrap
            }
            state.recruitmentHtml?.let {
                if (isAdmin) {
                    child(EditorComponent::class) {
                        attrs.text = it
                        attrs.coroutineScope = props.coroutineScope
                        attrs.url = "htmlPages/Recruitment.html"
                    }
                }
                    styledDiv {
                        css {
                            margin(top = 13.px)
                            textAlign = TextAlign.center
                            fontSize = 16.pt
                            fontWeight = FontWeight.bold
                        }
                        attrs["dangerouslySetInnerHTML"] = InnerHTML(it)
                    }
                } ?: run { loading() }

            styledDiv {
                css {
                    backgroundColor = Color("#F5F5F5")
                    width = 80.pct
                    borderRadius = 10.px
                }
                styledForm {
                    css {
                        display = Display.flex
                        justifyContent = JustifyContent.spaceAround
                        flexWrap = FlexWrap.wrap
                        child("div") {
                            float = Float.left
                            width = 45.pct
                            child("h3") {
                                fontFamily = "PT"
                            }
                            child("input") {
                                width = 95.pct
                            }
                        }
                    }
                    attrs.onSubmitFunction = { event ->
                        event.preventDefault()
                        event.stopPropagation()
                        val recruitmentService = RecruitmentService(coroutineContext)
                        props.coroutineScope.launch {
                            var formIsCompleted = true
                            state.inputs.values.forEach {
                                if (it.isRed) {
                                    formIsCompleted = false
                                }
                            }
                            if (formIsCompleted) {
                                recruitmentService.addRecruitment(
                                    //TODO: Идея пришла
                                    // Мб тут надо делать своего рода десериализацию
                                    // вместо вызова конструктора
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
                    formComponent(this)
//                    child(FormComponent::class) {
//                        attrs.inputs = state.inputs
//                        attrs.updateState = { key: String, value: String, isRed: Boolean ->
//                            setState {
//                                state.inputs[key]!!.inputValue = value
//                                state.inputs[key]!!.isRed = isRed
//                            }
//                        }
//                    }
                }
            }
            if (isAdmin) {
                styledDiv {
                    if (state.recruitments != null) {
                        state.recruitments!!.forEach { dto ->
                            styledDiv {
                                +dto.toString()
                                adminButton(AdminButtonType.Delete) {
                                    val recruitmentService = RecruitmentService(coroutineContext)
                                    props.coroutineScope.launch {
                                        recruitmentService.deleteRecruitment(dto.id!!)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Recruitment.formComponent(builder: RBuilder) {
    builder.child(FormComponent::class) {
        attrs.inputs = state.inputs
        attrs.updateState = { key: String, value: String, isRed: Boolean ->
            setState {
                state.inputs[key]!!.inputValue = value
                state.inputs[key]!!.isRed = isRed
            }
        }
    }
}