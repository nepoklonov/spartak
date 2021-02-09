package pageComponents

import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.html.id
import react.*
import react.dom.div

@JsModule("@ckeditor/ckeditor5-react")
external val CKEditor: RClass<CKEditorProps>

@JsModule("@ckeditor/ckeditor5-build-decoupled-document")
external val DecoupledEditor: RClass<CKEditorProps>

external interface CKEditorProps: RProps{
    var editor: dynamic
    var data: String
    var config: dynamic
    var onInit: (dynamic) -> Unit
    var onChange: (dynamic, dynamic) -> Unit
    var title: String
    var author: String
    var text: String
}

class CKEditorComponent: RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        div {
            div {
                attrs.id = "editor-toolbar"
            }
            div {
                attrs.id = "editor-content"
                CKEditor {
                    attrs.editor = DecoupledEditor
                    attrs.data = "однажды тут будет props.text"
                    attrs.config = js(
                        """{
                        placeholder: 'Your text..',
                        toolbar: [ 'bold', 'italic', 'link', 'bulletedList', 'numberedList', 'blockQuote' ]
                    }"""
                    )
                    attrs.onInit = {
                        val toolbar = it.ui.view.toolbar.element
                        document.getElementById("editor-toolbar")!!.appendChild(toolbar)
                    }
                    attrs.onChange = fun(_, editor) {
                        localStorage.setItem("draftText", editor.getData())
                    }
                }
            }
        }
    }
}