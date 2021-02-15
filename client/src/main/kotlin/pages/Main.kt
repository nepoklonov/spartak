package pages

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import pageComponents.CKEditorComponent
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
            val mainHtml = try {
                htmlService.getHtml("htmlPages/Main.html")
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }

            setState {
                this.mainHtml = mainHtml
            }
        }
    }

    override fun RBuilder.render() {
        if ( (state.mainHtml != null)) {
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

