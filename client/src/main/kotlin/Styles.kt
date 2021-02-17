
import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import kotlinx.css.properties.TextDecoration
import kotlinx.html.BUTTON
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
    val header by css {
        fontSize = 32.pt
        lineHeight = LineHeight.normal
        gridArea = "header"
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
        margin(left=0.px)
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