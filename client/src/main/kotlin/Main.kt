import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.css.*
import kotlinx.html.id
import react.buildElements
import react.dom.render
import react.router.dom.browserRouter
import react.router.dom.route
import styled.css
import styled.styledDiv
import view.FooterComponent
import view.HeaderComponent
import view.MainNavigationComponent
import view.MainNavigationProps
import kotlin.coroutines.CoroutineContext

private class Application : CoroutineScope {
    override val coroutineContext: CoroutineContext = Job()


    fun start() {
        document.getElementById("react-app")?.let {
            render(buildElements {
                browserRouter {
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
                            }
                            child(HeaderComponent::class) {}
                            route<MainNavigationProps>("/:selectedString") { props ->
                                child(MainNavigationComponent::class) {
                                    attrs.selectedString = props.match.params.selectedString
                                }
                            }
                            child(Router::class) {
                                attrs.coroutineScope = this@Application
                            }
                            child(FooterComponent::class) {}
                        }
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