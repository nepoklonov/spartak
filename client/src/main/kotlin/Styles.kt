
import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.css.properties.TextDecoration
import kotlinx.html.BUTTON
import kotlinx.html.H1
import react.RBuilder
import styled.*
import pageComponents.ColorSpartak

object Styles : StyleSheet("main") {
    val header by css {
        textAlign = TextAlign.center
        fontSize = 32.pt
        lineHeight = LineHeight.normal
    }

    val smallHeader by css {
        textAlign = TextAlign.center
        fontSize = 18.pt
        lineHeight = LineHeight.normal
    }

    val tableHeader by css {
        backgroundColor = ColorSpartak.LightGrey.color
        fontSize = 18.px
        padding(10.px)
    }

    val button by css{
        border = "none"
        textDecoration = TextDecoration.none
        fontFamily = "Russo"
        color = Color.white
        fontSize = 14.pt
        padding = 15.px.toString()
        paddingLeft = 50.px
        paddingRight = 50.px
        cursor = Cursor.pointer
    }

}

fun RBuilder.headerText(block: StyledDOMBuilder<H1>.() -> Unit) = styledH1 {
    css {
        +Styles.header
    }
    block()
}

fun RBuilder.smallHeaderText(block: StyledDOMBuilder<H1>.() -> Unit) = styledH1 {
    css {
        +Styles.smallHeader
    }
    block()
}
fun RBuilder.tableHeader(block: StyledDOMBuilder<H1>.() -> Unit) = styledH1 {
    css {
        +Styles.tableHeader
    }
    block()
}

fun RBuilder.redButtonSpartak(block: StyledDOMBuilder<BUTTON>.() -> Unit) = styledButton {
    css {
        backgroundColor = ColorSpartak.Red.color
        +Styles.button

    }
    block()
}

fun RBuilder.greyButtonSpartak(block: StyledDOMBuilder<BUTTON>.() -> Unit) = styledButton {
    css {
        backgroundColor = ColorSpartak.Grey.color
        +Styles.button
    }
    block()
}