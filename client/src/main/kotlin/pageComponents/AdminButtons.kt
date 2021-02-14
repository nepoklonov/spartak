package pageComponents

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

val Images = mapOf("add" to "/images/add.png", "edit" to "/images/edit.png", "delete" to "/images/delete.png")

external interface AdminButtonComponentProps: RProps{
    var updateState: () -> Unit
    var type: String
}


class AdminButtonComponent: RComponent<AdminButtonComponentProps, RState>() {
    override fun RBuilder.render(){
        styledImg(src = Images[props.type]){
            attrs.onClickFunction = { props.updateState() }
            css{
                width = 20.px
                cursor = Cursor.pointer
            }
        }
    }
}

