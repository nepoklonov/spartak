@file:JsModule("@ckeditor/ckeditor5-react")
package modules

import react.RClass
import react.RProps

external interface CKEditorProps: RProps {
    var editor: dynamic
    var data: String
    var config: dynamic
    var onReady: (dynamic) -> Unit
    var onChange: (dynamic, dynamic) -> Unit
    var title: String
    var text: String
}

@JsName("CKEditor")
external val CKEditor: RClass<CKEditorProps>