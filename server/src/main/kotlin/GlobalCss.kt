import kotlinx.css.*
import kotlinx.css.Float
import kotlinx.css.properties.TextDecoration
import kotlinx.css.properties.lh

val globalCss = CSSBuilder().apply {
    fontFace {
        fontFamily = "Russo"
        put("src", "url('/fonts/Russo-One.ttf')")
    }
    fontFace {
        fontFamily = "PT"
        put("src", "url('/fonts/PT-Sans.ttf')")
    }

    body {
        margin(0.px)
        padding(0.px)

        fontSize = 13.px
        fontFamily =
            "-system-ui, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Oxygen, Ubuntu, Cantarell, Droid Sans, Helvetica Neue, BlinkMacSystemFont, Segoe UI, Roboto, Oxygen, Ubuntu, Cantarell, Droid Sans, Helvetica Neue, Arial, sans-serif"

        lineHeight = 20.px.lh
    }
    h1 {
        fontFamily = "Russo"
        fontSize = 42.667.px
        marginLeft = 50.px
        fontWeight = FontWeight.normal
    }
    h2 {
        fontFamily = "Russo"
        fontWeight = FontWeight.normal
    }
    h3 {
        fontFamily = "Russo"
        fontWeight = FontWeight.normal
    }
    "*" {
        fontFamily = "PT"
        fontSize = 20.px
        lineHeight = 24.px.lh
    }
    //TODO: убрать дублирование здесь и в Styles.kt
    button {
        backgroundColor = Color("#9D0707")
        hover {
            backgroundColor = Color("#660c0c")
        }
        border = "none"
        textDecoration = TextDecoration.none
        fontFamily = "Russo"
        color = Color.white
        fontSize = 14.pt
        padding = 15.px.toString()
        paddingLeft = 50.px
        paddingRight = 50.px
        cursor = Cursor.pointer
        margin(10.px, 0.px)
    }
    rule(".news-img") {
        width = 32.pct
        padding = 1.pct.toString()
        height = LinearDimension.auto
        float = Float.left
    }
    rule(".news-div") {
        padding = 1.pct.toString()
        minWidth = 30.pct
    }
    rule(".summer-camp-img, .main-img, .ck-content .image") {
        width = 40.pct
        padding = 50.px.toString()
        height = LinearDimension.auto
    }
    rule(".summer-camp-div, .main") {
        display = Display.flex
        justifyContent = JustifyContent.spaceAround
    }
    rule(".summer-camp-content, .main-content") {
        padding = 50.px.toString()
        alignContent = Align.center
        display = Display.block
    }
    rule(".ck-content p, .ck-content h2") {
        alignContent = Align.center
        display = Display.block
    }
    rule(".recruitment-left"){
        float = Float.left
        width = 30.pct
    }
    rule(".recruitment-right"){
        float = Float.right
        width = 30.pct
    }
    rule(".recruitment"){
        display = Display.flex
        justifyContent = JustifyContent.spaceBetween
        padding = 5.pct.toString()
    }
}