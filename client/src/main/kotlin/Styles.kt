
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import kotlinx.html.BUTTON
import kotlinx.html.ButtonType
import kotlinx.html.H1
import react.RBuilder
import styled.*

object Styles : StyleSheet("main") {
    val header by css {
        fontFamily = "Russo"
    }
}

fun RBuilder.headerText(block: StyledDOMBuilder<H1>.() -> Unit) = styledH1 {
    css {
        +Styles.header
    }
    block()
}
fun RBuilder.buttonSpartak(block: StyledDOMBuilder<BUTTON>.() -> Unit) = styledButton{
    css {
        border = "none"
        textDecoration = TextDecoration.none
        color = Color.white
        fontSize = 16.pt
        padding = 15.px.toString()
        paddingLeft = 50.px
        paddingRight = 50.px
        focus {
            outline =  Outline.none
        }
    }
    block()
}