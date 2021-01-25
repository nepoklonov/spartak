import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import react.buildElements
import react.dom.render
import styled.styledDiv
import view.ApplicationComponent
import kotlinx.browser.document
import react.router.dom.RouteComponent
import react.router.dom.browserRouter
import routes.Routes
import view.AuthFormComponent

import kotlin.coroutines.CoroutineContext

private class Application : CoroutineScope {
    override val coroutineContext: CoroutineContext = Job()

    fun start() {
        document.getElementById("react-app")?.let {
            render(buildElements {
                child(Routes::class) {
                    styledDiv { +"Let's check!" }
                    child(ApplicationComponent::class) {
                        attrs.coroutineScope = this@Application
                    }
                    child(AuthFormComponent::class) {
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