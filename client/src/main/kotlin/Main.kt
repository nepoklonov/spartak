
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.css.*
import kotlinx.html.id
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
                    //TODO: вынести в отдельный метод
                    styledDiv {
                        css {
                            position = Position.fixed
                            backgroundImage = Image("url(/images/background.jpg)")
                            backgroundSize = "cover"
                            opacity = 0.8
                            width = 100.pct
                            height = 100.pct
                            child("div") {
                                fontFamily = "PT"
                            }
                        }
                    }
                    styledDiv {
                        attrs.id = "root"
                        css {
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