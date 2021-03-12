package pages

import adminPageComponents.EditorComponent
import kotlinext.js.jsObject
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import loading
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
            val summerCampHtml = htmlService.getHtml("htmlPages/SummerCamp.html")
            setState{
                this.summerCampHtml = summerCampHtml
            }
        }
    }

    override fun RBuilder.render() {
        if (document.cookie.contains("role=admin") && (state.summerCampHtml != null)) {
            child(EditorComponent::class) {
                attrs.text = state.summerCampHtml!!
                attrs.coroutineScope = props.coroutineScope
                attrs.url = "htmlPages/SummerCamp.html"
            }
        }
        styledDiv {
            state.summerCampHtml?.let {
                attrs["dangerouslySetInnerHTML"] = jsObject<InnerHTML> { __html = state.summerCampHtml!! }
            } ?: run { loading() }
        }
    }
}