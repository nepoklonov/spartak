package modules

import kotlinx.coroutines.CoroutineScope
import react.RClass
import react.RProps
import react.RState

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