package view

import kotlinx.coroutines.CoroutineScope
import kotlinx.css.*
import kotlinx.html.id
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css
import styled.styledDiv

fun CSSBuilder.gridTemplateAreas(vararg s: String) {
    gridTemplateAreas = GridTemplateAreas(s.joinToString("") { "\"$it\" " })
}

external interface RootProps : RProps {
    var coroutineScope: CoroutineScope
}


open class RootComponent<T, U> : RComponent<RootProps, RState>() {
    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun RBuilder.render() {
        styledDiv {
            css {
                backgroundImage = Image("url(/images/background.jpg)")
                width = 100.pct
            }
            styledDiv {
                attrs.id = "root"
                css {
                    marginLeft = 100.px
                    marginRight = 100.px
                    backgroundColor = rgba(255, 255, 255, 0.5)
//                    backgroundColor = Color("#484444")

                }
                child(HeaderComponent::class) {}
                child(NavigationComponent::class) {}
                child(FooterComponent::class) {}
            }
        }
    }
}
