package pageComponents

import kotlinx.css.px
import kotlinx.css.width
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css
import styled.styledImg

external interface AddButtonComponentProps: RProps{
    var updateState: () -> Unit
}


class AddButtonComponent: RComponent<AddButtonComponentProps, RState>() {
    override fun RBuilder.render(){
        styledImg(src = "/images/add.png"){
            attrs.onClickFunction = { props.updateState() }
            css{
                width = 30.px
            }
        }
    }
}

external interface EditButtonComponentProps: RProps{
    var updateState: () -> Unit
}


class EditButtonComponent: RComponent<EditButtonComponentProps, RState>() {
    override fun RBuilder.render(){
        styledImg(src = "/images/edit.png"){
            attrs.onClickFunction = { props.updateState() }
            css{
                width = 30.px
            }
        }
    }
}

external interface DeleteButtonComponentProps: RProps{
    var updateState: () -> Unit
}


class DeleteButtonComponent: RComponent<DeleteButtonComponentProps, RState>() {
    override fun RBuilder.render(){
        styledImg(src = "/images/delete.png"){
            attrs.onClickFunction = { props.updateState() }
            css{
                width = 30.px
            }
        }
    }
}

