import kotlinext.js.invoke
import kotlinx.css.*
import styled.createGlobalStyle

object GlobalStyles {
    fun inject() {
        val styles = CSSBuilder(allowClasses = true).apply {
            body {
                margin(0.px)
                padding(0.px)
            }
            rule("@font-face"){
                fontFamily = "Russo"
                put("src", "url('/fonts/Russo-One.ttf')")
            }
        }

        createGlobalStyle(styles.toString())
    }
}