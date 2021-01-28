
import kotlinx.coroutines.CoroutineScope
import pages.*
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.router.dom.route
import react.router.dom.switch

external interface RouterProps : RProps {
    var coroutineScope: CoroutineScope
}

class Router : RComponent<RouterProps, RState>() {
    fun RBuilder.appWithRouter() {
        switch {
//                                    redirect(from = "/", to = "/main")
            route("/main"){
                child(Main::class){
                    attrs.coroutineScope = props.coroutineScope
                }
            }
            route("/admin", Admin::class, exact = true)
            route("/page", SomePage::class, exact = true)
            route("/news/feed"){
                child(News::class){
                    attrs.coroutineScope = props.coroutineScope
                    attrs.selectedNewsId = "feed"
                }
            }

            route<NewsProps>("/news/:selectedNewsId"){idProps ->
                child(News::class){
                    attrs.coroutineScope = props.coroutineScope
                    attrs.selectedNewsId = idProps.match.params.selectedNewsId
                }
            }

            route<GamesProps>("/games/:selectedChampionship") {championshipProps ->
                child(Games::class) {
                    attrs.coroutineScope = props.coroutineScope
                    attrs.selectedChampionship = championshipProps.match.params.selectedChampionship
                }
            }

            route("/club", Club::class, exact = true)
            route("/recruitment", Recruitment::class, exact = true)
            route("/workouts", Workouts::class, exact = true)
            route("/summerCamp") {
                child(SummerCamp::class) {
                    attrs.coroutineScope = props.coroutineScope
                }
            }
            route("/gallery", Gallery::class, exact = true)
        }
    }

    override fun RBuilder.render() {
        appWithRouter()
    }
}