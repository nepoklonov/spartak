import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pages.*
import react.*
import react.router.dom.redirect
import react.router.dom.route
import react.router.dom.switch
import services.MainNavigationService

external interface RouterProps : RProps {
    var coroutineScope: CoroutineScope
}

class RouterState : RState {
    var error: Throwable? = null
    var mainNavigationList: List<String>? = null
}

class Router : RComponent<RouterProps, RouterState>() {
    override fun componentDidMount() {
        val mainNavigationService = MainNavigationService(props.coroutineScope.coroutineContext)
        props.coroutineScope.launch {
            val mainNavigationList = try {
                mainNavigationService.getFirstLinks()
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }
            setState {
                this.mainNavigationList = mainNavigationList
            }
        }
    }

    fun RBuilder.appWithRouter() {
        switch {
            redirect(from = "/", to = "/main", exact = true)
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
            redirect(from = "/news", to = "/news/feed", exact = true)
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

            if (state.mainNavigationList != null) {
                redirect(from = "/games", to = "/games/${state.mainNavigationList!![0]}", exact = true)
            }
            route<GamesProps>("/games/:selectedChampionship") { championshipProps ->
                child(Games::class) {
                    attrs.coroutineScope = props.coroutineScope
                    attrs.selectedChampionship = championshipProps.match.params.selectedChampionship
                }
            }
            if (state.mainNavigationList != null) {
                redirect(from = "/teams", to = "/teams/${state.mainNavigationList!![1]}", exact = true)
            }
            route<TeamsProps>("/teams/:selectedTeam") { teamsProps ->
                child(Teams::class) {
                    attrs.coroutineScope = props.coroutineScope
                    attrs.selectedTeam = teamsProps.match.params.selectedTeam
                }
            }
            route("/recruitment") {
                child(Recruitment::class) {
                    attrs.coroutineScope = props.coroutineScope
                }
            }
            if (state.mainNavigationList != null) {
                redirect(from = "/workouts", to = "/workouts/${state.mainNavigationList!![2]}", exact = true)
            }
            route<WorkoutsProps>("/workouts/:selectedWorkout") { workoutsProps ->
                child(Workouts::class) {
                    attrs.coroutineScope = props.coroutineScope
                    attrs.selectedWorkout = workoutsProps.match.params.selectedWorkout
                }
            }

            route("/summerCamp") {
                child(SummerCamp::class) {
                    attrs.coroutineScope = props.coroutineScope
                }
            }
            if (state.mainNavigationList != null) {
                redirect(from = "/gallery", to = "/gallery/${state.mainNavigationList!![3]}", exact = true)
            }
            route<GalleryProps>("/gallery/:selectedGallerySection") { galleryProps ->
                child(Gallery::class) {
                    attrs.coroutineScope = props.coroutineScope
                    attrs.selectedGallerySection = galleryProps.match.params.selectedGallerySection
                }
            }
        }
    }

    override fun RBuilder.render() {
        //TODO: вынести содержимое appWithRouter в render
        appWithRouter()
    }
}