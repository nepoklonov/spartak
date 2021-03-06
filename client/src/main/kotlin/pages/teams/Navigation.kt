package pages.teams

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import loading
import model.NavigationDTO
import model.TeamDTO
import model.TrainerDTO
import react.*
import react.router.dom.route
import services.TeamService
import services.TrainerService
import structure.SmallNavigation
import structure.SmallNavigationProps

external interface TeamsNavigationProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedTeam: String
}

class TeamsNavigationState : RState {
    var navigationList: List<NavigationDTO>? = null
}

class TeamsNavigation : RComponent<TeamsNavigationProps, TeamsNavigationState>() {
    init {
        state = TeamsNavigationState()
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val teamService = TeamService(coroutineContext)

        props.coroutineScope.launch {
            val navigationList = teamService.getNavigationList()

            setState {
                this.navigationList = navigationList
            }
        }
    }

    override fun RBuilder.render() {
        val teamService = TeamService(coroutineContext)
        val trainerService = TrainerService(coroutineContext)

        state.navigationList?.let { navigationList ->
            route<SmallNavigationProps>("/teams/:selectedLineLink") { selectedLineLink ->
                child(SmallNavigation::class) {
                    attrs.navLines = navigationList
                    attrs.selectedLineLink = selectedLineLink.match.params.selectedLineLink
                    attrs.deleteFunction = { id: Int ->
                        props.coroutineScope.launch {
                            teamService.deleteTeam(id)
                        }
                    }
                    attrs.editFunction = { id: Int, listOfInputValues: List<String> ->
                        props.coroutineScope.launch {
                            teamService.editTeam(
                                TeamDTO(
                                    id,
                                    listOfInputValues[0],
                                    listOfInputValues[1],
                                    true,
                                    props.selectedTeam
                                )
                            )
                        }
                    }
                    attrs.addFunction = { listOfInputValues ->
                        props.coroutineScope.launch {
                            teamService.addTeam(
                                TeamDTO(
                                    null,
                                    listOfInputValues[0],
                                    listOfInputValues[1],
                                    true,
                                    null
                                    )
                            )
                                trainerService.addTrainer(
                                    TrainerDTO(
                                        null,
                                        listOfInputValues[1],
                                        "",
                                        "",
                                        ""
                                    )
                                )
                        }
                    }
                }
            }
        } ?: run { loading() }
    }
}
