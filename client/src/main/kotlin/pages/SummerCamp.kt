package pages

import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pageComponents.CKEditorComponent
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
        if (document.cookie.contains("role=admin") && (state.summerCampHtml != null)) {
            child(CKEditorComponent::class) {
                attrs.text = state.summerCampHtml!!
                attrs.coroutineScope = props.coroutineScope
                attrs.url = "htmlPages/SummerCamp.html"
            }
        }
        styledDiv {
            if (state.summerCampHtml != null) {
                attrs["dangerouslySetInnerHTML"] = InnerHTML(state.summerCampHtml!!)
            } else {
                +"загрузка..."
            }
        }
    }
}