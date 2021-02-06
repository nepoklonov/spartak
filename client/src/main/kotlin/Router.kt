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
            route("/main") {
                child(Main::class) {
                    attrs.coroutineScope = props.coroutineScope
                }
            }
            route("/admin", exact = true) {
                child(Admin::class) {
                    attrs.coroutineScope = props.coroutineScope
                }
            }
            route("/news/feed") {
                child(News::class) {
                    attrs.coroutineScope = props.coroutineScope
                    attrs.selectedNewsId = "feed"
                }
            }

            route<NewsProps>("/news/:selectedNewsId") { idProps ->
                child(News::class) {
                    attrs.coroutineScope = props.coroutineScope
                    attrs.selectedNewsId = idProps.match.params.selectedNewsId
                }
            }

            route<GamesProps>("/games/:selectedChampionship") { championshipProps ->
                child(Games::class) {
                    attrs.coroutineScope = props.coroutineScope
                    attrs.selectedChampionship = championshipProps.match.params.selectedChampionship
                }
            }

            route<TeamsProps>("/teams/:selectedTeam") {teamsProps ->
                child(Teams::class) {
                    attrs.coroutineScope = props.coroutineScope
                    attrs.selectedTeam = teamsProps.match.params.selectedTeam
                }
            }
            route("/recruitment"){
                child(Recruitment::class){
                    attrs.coroutineScope = props.coroutineScope
                }
            }
            route<WorkoutsProps>("/workouts/:selectedWorkout"){ workoutsProps ->
                child(Workouts::class){
                    attrs.coroutineScope = props.coroutineScope
                    attrs.selectedWorkout = workoutsProps.match.params.selectedWorkout
                }
            }

            route("/summerCamp") {
                child(SummerCamp::class) {
                    attrs.coroutineScope = props.coroutineScope
                }
            }
            route<GalleryProps>("/gallery/:selectedGallerySection"){ galleryProps ->
                child(Gallery::class){
                    attrs.coroutineScope = props.coroutineScope
                    attrs.selectedGallerySection = galleryProps.match.params.selectedGallerySection
                }
            }
        }
    }

    override fun RBuilder.render() {
        appWithRouter()
    }
}