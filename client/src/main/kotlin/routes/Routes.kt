package routes

import pages.Admin
import pages.Main
import pages.SomePage
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import react.router.dom.*
import kotlin.coroutines.coroutineContext

class Routes : RComponent<RProps, RState>() {
    fun RBuilder.appWithRouter() {
        browserRouter {
            div {
                switch {
                    route("/", Main()::class, exact = true)
                    route("/admin", Admin()::class, exact = true)
                    route("/page", SomePage::class, exact = true)
                }
            }
        }
    }

    override fun RBuilder.render() {
            appWithRouter()
    }
}



