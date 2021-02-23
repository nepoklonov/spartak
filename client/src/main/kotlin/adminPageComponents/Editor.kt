package adminPageComponents

import modules.CKEditor
import modules.CKEditorProps
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.id
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.events.Event
import pageComponents.buttonMain
import react.*
import react.dom.div
import services.HtmlService
import styled.styledForm

@JsModule("@ckeditor/ckeditor5-build-decoupled-document")
external val DecoupledEditor: RClass<CKEditorProps>
external interface EditorProps : RProps {
    var text: String?
    var url: String
    var coroutineScope: CoroutineScope
}
external interface EditorState : RState {
    var text: String?
}
//TODO: вынести вот это ^ в отдельный файл в тот же пакет, где будет CKEditor

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
                        document.getElementById("editor-toolbar")!!.appendChild(toolbar)
                    }
                    attrs.onChange = fun(_, editor) {
                        state.text=attrs.data
                        localStorage.setItem("draftText", editor.getData())
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