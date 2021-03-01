package adminPageComponents

import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.launch
import kotlinx.html.id
import kotlinx.html.js.onSubmitFunction
import modules.CKEditor
import modules.DecoupledEditor
import modules.EditorProps
import modules.EditorState
import org.w3c.dom.Node
import org.w3c.dom.events.Event
import pageComponents.buttonMain
import react.RBuilder
import react.RComponent
import react.dom.div
import services.HtmlService
import styled.styledForm

class CKEditorComponent: RComponent<EditorProps, EditorState>() {
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
                    attrs.data = props.text!!
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
                        state.text=attrs.data
                        localStorage.setItem("draftText", editor.getData() as String)
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