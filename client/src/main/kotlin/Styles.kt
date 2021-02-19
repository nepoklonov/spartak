import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.css.properties.TextDecoration
import kotlinx.css.properties.boxShadow
import kotlinx.html.BUTTON
import kotlinx.html.DIV
import kotlinx.html.H1
import pageComponents.ColorSpartak
import react.RBuilder
import styled.*
import kotlin.reflect.KProperty

fun CSSBuilder.gridTemplateAreas(vararg s: String) {
    gridTemplateAreas = GridTemplateAreas(s.joinToString("") { "\"$it\" " })
}

@Suppress("UNCHECKED_CAST")
private class CSSProperty<T>(private val default: (() -> T)? = null) {
    operator fun getValue(thisRef: StyledElement, property: KProperty<*>): T {
        default?.let { default ->
            if (!thisRef.declarations.containsKey(property.name)) {
                thisRef.declarations[property.name] = default() as Any
            }
        }
        return thisRef.declarations[property.name] as T
    }

    operator fun setValue(thisRef: StyledElement, property: KProperty<*>, value: T) {
        thisRef.declarations[property.name] = value as Any
    }
}

var StyledElement.gridArea: String by CSSProperty()


object Styles : StyleSheet("main") {
    val grid by css {
        display = Display.grid
        gridTemplateAreas(
            ". header",
            "navigation content",
            ". content"
        )
        gridTemplateRows = GridTemplateRows("auto auto")
        gridTemplateColumns = GridTemplateColumns("325px auto")
    }

    val header by css {
        fontSize = 32.pt
        lineHeight = LineHeight.normal
        gridArea = "header"
    }

    val navigation by css {
        gridArea = "navigation"
        position = Position.sticky
        backgroundColor = Color.white
        boxShadow(color = rgba(0, 0, 0, 0.25), offsetX = 0.px, offsetY = 4.px, blurRadius = 4.px)
    }

    val content by css {
        gridArea = "content"
        paddingLeft = 50.px
        paddingRight = 50.px
    }

    val smallHeader by css {
        textAlign = TextAlign.center
        fontSize = 18.pt
        lineHeight = LineHeight.normal
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

    val tableGrid by css{
        display = Display.grid
        gridTemplateAreas(
            "date time teamA teamB stadium result"
        )
        gridTemplateColumns = GridTemplateColumns("1fr 1fr 1fr 1fr 1fr 1fr")
    }

    val tableHeader by css {
        height = 60.px
        width = 100.pct
        marginTop = 3.px
        backgroundColor = ColorSpartak.LightGrey.color
        fontFamily = "Russo"
        fontSize = 20.px
        padding(10.px)
        display = Display.flex
        alignItems = Align.center
        boxShadow(
            color = rgba(0, 0, 0, 0.25),
            offsetX = 0.px,
            offsetY = 4.px,
            blurRadius = 4.px
        )
    }

    val tableContent by css {
        minHeight = 70.px
        width = 100.pct
        padding(10.px)
        marginTop = 3.px
        backgroundColor = Color.white
        boxShadow(
            color = rgba(0, 0, 0, 0.25),
            offsetX = 0.px,
            offsetY = 4.px,
            blurRadius = 4.px
        )
        display = Display.flex
        alignItems = Align.center
    }
}

fun RBuilder.grid(block: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
    css {
        +Styles.grid
    }
    block()
}

fun RBuilder.header(block: StyledDOMBuilder<H1>.() -> Unit) = styledH1 {
    css {
        +Styles.header
    }
    block()
}

fun RBuilder.content(block: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
    css {
        +Styles.content
    }
    block()
}

fun RBuilder.navigation(block: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
    css {
        +Styles.navigation
    }
    block()
}

fun RBuilder.smallHeaderText(block: StyledDOMBuilder<H1>.() -> Unit) = styledH1 {
    css {
        +Styles.smallHeader
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

fun RBuilder.gamesTableHeader(block: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
    css {
        +Styles.tableGrid
        +Styles.tableHeader
    }
    block()
}

fun RBuilder.workoutsTableHeader(block: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
    css {
        +Styles.tableHeader
    }
    block()
}


fun RBuilder.gamesTableContent(block: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
    css {
        +Styles.tableGrid
        +Styles.tableContent
    }
    block()
}
fun RBuilder.workoutsTableContent(block: StyledDOMBuilder<DIV>.() -> Unit) = styledDiv {
    css {
        +Styles.tableContent
    }
    block()
}