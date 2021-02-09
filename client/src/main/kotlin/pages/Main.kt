package pages

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import react.*
import react.dom.InnerHTML
import react.dom.div
import services.HtmlService
import styled.styledDiv


@JsModule("@ckeditor/ckeditor5-react")
external val CKEditor: RClass<MainProps>

@JsModule("@ckeditor/ckeditor5-build-decoupled-document")
external val DecoupledEditor: RClass<MainProps>

external interface MainProps : RProps {
    var coroutineScope: CoroutineScope
    var editor: dynamic
    var data: String
    var config: dynamic
    var onInit: (dynamic) -> Unit
    var onChange: (dynamic, dynamic) -> Unit
    var title: String
    var author: String
    var text: String
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

            setState() {
                this.mainHtml = mainHtml
            }
        }
    }

    override fun RBuilder.render() {
        div{
            CKEditor{
                attrs.editor = DecoupledEditor
                attrs.data = "а почему ниче не работает("
            }
        }
//        div {
//            div {
//                attrs.id = "editor-toolbar"
//            }
//            div {
//                attrs.id = "editor-content"
//                CKEditor {
//                    attrs.editor = DecoupledEditor
//                    attrs.data = props.text
//                    attrs.config = js(
//                        """{
//                        placeholder: 'Your text..',
//                        toolbar: [ 'bold', 'italic', 'link', 'bulletedList', 'numberedList', 'blockQuote' ]
//                    }"""
//                    )
//                    attrs.onInit = {
//                        val toolbar = it.ui.view.toolbar.element
//                        document.getElementById("editor-toolbar")!!.appendChild(toolbar)
//                    }
//                    attrs.onChange = fun(_, editor) {
//                        localStorage.setItem("draftText", editor.getData())
//                    }
//                }
//            }
//        }

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

