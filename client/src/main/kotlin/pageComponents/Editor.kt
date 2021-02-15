package pageComponents

import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.id
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.events.Event
import react.*
import react.dom.div
import redButtonSpartak
import services.AdminService
import services.HtmlService
import styled.styledForm
import kotlin.coroutines.CoroutineContext

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
class CKEditorComponent: RComponent<EditorProps, EditorState>() {
    private val coroutineContext
        get() = props.coroutineScope.coroutineContext
    private fun handleSubmit(url: String, htmlService: HtmlService, event: Event) {
        event.preventDefault()
        event.stopPropagation()
        props.coroutineScope.launch {
            htmlService.editHtml(url)
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
                    handleSubmit(props.url, htmlService, it)
                }
                child(ButtonMain::class) {
                    attrs.text = "Отправить"
                }
            }
        }
    }
}