
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.css.*
import pageComponents.background
import react.buildElements
import react.dom.render
import react.router.dom.browserRouter
import react.router.dom.route
import structure.FooterComponent
import structure.HeaderComponent
import structure.MainNavigationProps
import styled.css
import styled.styledDiv
import kotlin.coroutines.CoroutineContext

private class Application : CoroutineScope {
    override val coroutineContext: CoroutineContext = Job()

    fun start() {
        document.getElementById("react-app")?.let {
            val spartakContext = react.createContext("Basic")
            spartakContext.Provider

            render(buildElements {
                browserRouter {
                    background()
                    styledDiv {
//                        attrs.id = "root"
                        css {
                            minWidth = 1060.px
                            position = Position.relative
                            marginLeft = 100.px
                            marginRight = 100.px
                            backgroundColor = rgba(255, 255, 255, 0.6)
                        }
                        route<MainNavigationProps>("/:selectedString") { props ->
                            child(HeaderComponent::class) {
                                attrs.selectedString = props.match.params.selectedString
                            }
                        }
                        child(Router::class) {
                            attrs.coroutineScope = this@Application
                        }
                        child(FooterComponent::class) { }
                    }
                }
            }, it)
        }
    }
}

fun main() {
    GlobalStyles.inject()
    Application().start()
}