package pages

import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import adminPageComponents.CKEditorComponent
import pageComponents.MainNews
import react.*
import react.dom.InnerHTML
import services.HtmlService
import styled.styledDiv

external interface MainProps : RProps {
    var coroutineScope: CoroutineScope
}

class MainState : RState {
    var error: Throwable? = null
    var mainHtml: String? = null
}

class Main : RComponent<MainProps, MainState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext


    override fun componentDidMount() {
        val htmlService = HtmlService(coroutineContext)

        props.coroutineScope.launch {
            val mainHtml = htmlService.getHtml("htmlPages/Main.html")
            setState {
                this.mainHtml = mainHtml
            }
        }
    }

    override fun RBuilder.render() {
        if (document.cookie.contains("role=admin") && (state.mainHtml != null)) {
            child(CKEditorComponent::class) {
                attrs.text = state.mainHtml!!
                attrs.url = "htmlPages/Main.html"
                attrs.coroutineScope = props.coroutineScope
            }
        }

        styledDiv {
            if (state.mainHtml != null) {
                attrs["dangerouslySetInnerHTML"] = InnerHTML(state.mainHtml!!)
            } else {
                +"загрузка..."
            }
        }
            child(MainNews::class) {
                attrs.coroutineScope = props.coroutineScope
            }
        }
}

