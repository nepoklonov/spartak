
import kotlinx.css.fontFamily
import kotlinx.html.H1
import react.RBuilder
import styled.StyleSheet
import styled.StyledDOMBuilder
import styled.css
import styled.styledH1

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