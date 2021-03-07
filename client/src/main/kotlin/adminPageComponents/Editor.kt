package adminPageComponents

import buttonMain
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.id
import kotlinx.html.js.onSubmitFunction
import modules.CKEditor
import modules.DecoupledEditor
import org.w3c.dom.Node
import org.w3c.dom.events.Event
import react.*
import react.dom.div
import services.HtmlService
import styled.styledForm

interface EditorProps : RProps {
    var text: String?
    var url: String
    var coroutineScope: CoroutineScope
}

interface EditorState : RState {
    var text: String?
}

class EditorComponent : RComponent<EditorProps, EditorState>() {
    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    private fun handleSubmit(url: String, content: String, htmlService: HtmlService, event: Event) {
        event.preventDefault()
        event.stopPropagation()
        props.coroutineScope.launch {
            htmlService.editHtml(url, content)
        }
    }

    override fun RBuilder.render() {
        val htmlService = HtmlService(coroutineContext)
        div {
            div {
                attrs.id = "editor-toolbar"
            }
            div {
                attrs.id = "editor-content"
                CKEditor {
                    attrs.editor = DecoupledEditor
                    attrs.data = state.text ?: props.text!!
                    attrs.config = js(
                        """{
                        placeholder: 'Your text..',
                        toolbar: [ 'bold', 'italic', 'link', 'bulletedList', 'numberedList', 'blockQuote' ]
                    }"""
                    )
                    attrs.onReady = {
                        val toolbar = it.ui.view.toolbar.element
                        document.getElementById("editor-toolbar")!!.appendChild(toolbar as Node)
                    }
                    attrs.onChange = fun(_, editor) {
                        setState {
                            text = editor.getData() as String
                        }
//                        localStorage.setItem("draftText", editor.getData() as String)
                    }
                }
            }
            styledForm {
                attrs.onSubmitFunction = {
                    handleSubmit(props.url, state.text!!, htmlService, it)
                }
                buttonMain("Отправить")
            }
        }
    }
}