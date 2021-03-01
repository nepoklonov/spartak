package pages.Games

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import model.NavigationDTO
import react.*
import react.router.dom.route
import services.GamesNavigationService
import structure.SmallNavigation
import structure.SmallNavigationProps

external interface GamesNavigationProps : RProps {
    var coroutineScope: CoroutineScope
}

class GamesNavigationState : RState {
    var gamesNavigationList: List<NavigationDTO>? = null
}

class GamesNavigation: RComponent<GamesNavigationProps, GamesNavigationState>() {
    init {
        state = GamesNavigationState()
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val gamesNavigationService = GamesNavigationService(coroutineContext)

        props.coroutineScope.launch {

            val gamesNavigationList = gamesNavigationService.getGamesNavigationList()

            setState{
                this.gamesNavigationList = gamesNavigationList
            }
        }
    }

    override fun RBuilder.render(){
        val gamesNavigationService = GamesNavigationService(coroutineContext)
        state.gamesNavigationList?.let { gamesNavigationList ->
            route<SmallNavigationProps>("/games/:selectedLineLink") { selectedLineLink ->
                child(SmallNavigation::class) {
                    attrs.navLines = gamesNavigationList
                    attrs.selectedLineLink = selectedLineLink.match.params.selectedLineLink
                    attrs.deleteFunction = { id: Int ->
                        props.coroutineScope.launch {
                            gamesNavigationService.deleteGamesSection(id)
                        }
                    }
                    attrs.editFunction = { id: Int, listOfInputValues: List<String> ->
                        props.coroutineScope.launch {
                            gamesNavigationService.editGamesSection(
                                NavigationDTO(
                                    id,
                                    listOfInputValues[0],
                                    listOfInputValues[1]
                                )
                            )
                        }
                    }
                    attrs.addFunction = { listOfInputValues ->
                        props.coroutineScope.launch {
                            gamesNavigationService.addGamesSection(
                                NavigationDTO(
                                    null,
                                    listOfInputValues[0],
                                    listOfInputValues[1]
                                )
                            )
                        }
                    }
                }
            }
        } ?: run {
            +"Загрузка..."
        }
    }
}