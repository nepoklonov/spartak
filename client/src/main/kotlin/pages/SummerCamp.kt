package pages

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import react.*
import react.dom.InnerHTML
import services.HtmlService
import styled.styledDiv

external interface SummerCampProps : RProps {
    var coroutineScope: CoroutineScope
}

class SummerCampState : RState {
    var error: Throwable? = null
    var summerCampHtml: String? = null
}

class SummerCamp : RComponent<SummerCampProps, SummerCampState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val htmlService = HtmlService(coroutineContext)

        props.coroutineScope.launch {
            val summerCampHtml = try {
                htmlService.getHtml("htmlPages/SummerCamp.html")
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }

            setState() {
                this.summerCampHtml = summerCampHtml
            }
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            if (state.summerCampHtml != null) {
                attrs["dangerouslySetInnerHTML"] = InnerHTML(state.summerCampHtml!!)
            } else {
                +"Страница не доступна"
            }
        }
    }
}