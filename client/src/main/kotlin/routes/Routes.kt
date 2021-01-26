package routes

import pages.Admin
import pages.Main
import pages.SomePage
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div
import react.router.dom.browserRouter
import react.router.dom.route
import react.router.dom.switch


class Routes : RComponent<RProps, RState>() {
    private fun RBuilder.appWithRouter() {
        browserRouter {
            div {
                switch {
                    route("/", Main::class, exact = true)
                    route("/admin", Admin::class, exact = true)
                    route("/page", SomePage::class, exact = true)
                }
            }
        }
    }

    override fun RBuilder.render() {
        appWithRouter()
    }
}



