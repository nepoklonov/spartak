package pages

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import react.*
import react.dom.InnerHTML
import services.HtmlService
import styled.css
import styled.styledDiv

external interface RecruitmentProps : RProps {
    var coroutineScope: CoroutineScope
}

class RecruitmentState : RState {
    var error: Throwable? = null
    var recruitmentHtml: String? = null
}

class Recruitment : RComponent<RecruitmentProps, RecruitmentState>() {
    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val htmlService = HtmlService(coroutineContext)

        props.coroutineScope.launch {
            val recruitmentHtml = try {
                htmlService.getHtml("htmlPages/Recruitment.html")
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }

            setState() {
                this.recruitmentHtml = recruitmentHtml
            }
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css{
                textAlign = TextAlign.center
                fontFamily = "PT"
                fontSize = 16.pt
                fontWeight = FontWeight.bold
            }
            if (state.recruitmentHtml != null) {
                attrs["dangerouslySetInnerHTML"] = InnerHTML(state.recruitmentHtml!!)
            } else {
                +"загрузка..."
            }
        }
    }
}