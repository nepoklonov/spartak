package adminPageComponents

import isAdmin
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.RBuilder
import styled.css
import styled.styledImg

enum class AdminButtonType(val src: String) {
    Add("/images/add.png"),
    Edit("/images/edit.png"),
    Delete("/images/delete.png")
}

fun RBuilder.adminButton(button: AdminButtonType, updateState: () -> Unit) {
    if (isAdmin) {
        styledImg(src = button.src) {
            attrs.onClickFunction = { updateState() }
            css {
                alignSelf = Align.flexStart
                width = 20.px
                cursor = Cursor.pointer
            }
        }
    }
}