package adminPageComponents

import kotlinx.css.Cursor
import kotlinx.css.cursor
import kotlinx.css.px
import kotlinx.css.width
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css
import styled.styledImg

enum class AdminButtonType(val src: String){
    Add("/images/add.png"),
    Edit("/images/edit.png"),
    Delete("/images/delete.png")
}

external interface AdminButtonComponentProps: RProps{
    var updateState: () -> Unit
    var type: AdminButtonType
}


class AdminButtonComponent: RComponent<AdminButtonComponentProps, RState>() {
    override fun RBuilder.render(){
        styledImg(src = props.type.src){
            attrs.onClickFunction = { props.updateState() }
            css{
                width = 20.px
                cursor = Cursor.pointer
            }
        }
    }
}

