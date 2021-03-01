
import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.css.properties.TextDecoration
import kotlinx.css.properties.boxShadow
import kotlinx.html.BUTTON
import kotlinx.html.DIV
import kotlinx.html.H1
import react.RBuilder
import styled.*

enum class ColorSpartak(val color: Color){
    Red(Color("#9D0707")),
    Grey(Color("#484444")),
    LightGrey(Color("#E0DEDF"))
}

object Styles : StyleSheet("main") {

    val shadowSpartak by css {
        boxShadow(
            color = rgba(0, 0, 0, 0.25),
            offsetX = 0.px,
            offsetY = 4.px,
            blurRadius = 4.px
        )
    }

    val button by css {
        textAlign = TextAlign.center
        border = "none"
        textDecoration = TextDecoration.none
        fontFamily = "Russo"
        color = Color.white
        fontSize = 14.pt
        padding = 15.px.toString()
        paddingLeft = 50.px
        paddingRight = 50.px
        cursor = Cursor.pointer
        margin(left = 0.px)
    }
}

fun RBuilder.grid(block: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
    css {
        display = Display.grid
        gridTemplateAreas(
            ". header",
            "navigation content",
            ". content"
        )
        gridTemplateRows = GridTemplateRows("auto auto")
        gridTemplateColumns = GridTemplateColumns("325px auto")
    }
    block()
}

fun RBuilder.header(block: StyledDOMBuilder<H1>.() -> Unit) = styledH1 {
    css {
        fontSize = 32.pt
        lineHeight = LineHeight.normal
        gridArea = "header"
    }
    block()
}

fun RBuilder.content(block: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
    css {
        gridArea = "content"
        paddingLeft = 50.px
        paddingRight = 50.px
    }
    block()
}

fun RBuilder.navigation(block: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
    css {
        gridArea = "navigation"
        backgroundColor = Color.white
        position = Position.sticky
        top = 30.px
        +Styles.shadowSpartak
    }
    block()
}

fun RBuilder.smallHeaderText(block: StyledDOMBuilder<H1>.() -> Unit) = styledH1 {
    css {
        textAlign = TextAlign.center
        fontSize = 18.pt
        lineHeight = LineHeight.normal
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

//fun RBuilder.greyButtonSpartak(block: StyledDOMBuilder<BUTTON>.() -> Unit) = styledButton {
//    css {
//        backgroundColor = ColorSpartak.Grey.color
//        +Styles.button
//    }
//    block()
//}

fun RBuilder.tableHeader(block: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
    css {
        height = 60.px
        marginTop = 3.px
        backgroundColor = ColorSpartak.LightGrey.color
        fontFamily = "Russo"
        fontSize = 20.px
        padding(10.px)
        display = Display.flex
        alignItems = Align.center
        +Styles.shadowSpartak
    }
    block()
}

fun RBuilder.tableContent(block: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
    css {
        minHeight = 70.px
        padding(10.px)
        marginTop = 3.px
        backgroundColor = Color.white
        display = Display.flex
        alignItems = Align.center
        +Styles.shadowSpartak
    }
    block()
}
