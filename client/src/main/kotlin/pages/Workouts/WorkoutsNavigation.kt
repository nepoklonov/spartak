package pages.Workouts

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import model.NavigationDTO
import react.*
import react.router.dom.route
import services.WorkoutsNavigationService
import structure.SmallNavigation
import structure.SmallNavigationProps

external interface WorkoutsNavigationProps : RProps {
    var coroutineScope: CoroutineScope
}

class WorkoutsNavigationState : RState {
    var workoutsNavigationList: List<NavigationDTO>? = null
}

class WorkoutsNavigation : RComponent<WorkoutsNavigationProps, WorkoutsNavigationState>() {
    init {
        state = WorkoutsNavigationState()
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val workoutsNavigationService = WorkoutsNavigationService(coroutineContext)
        props.coroutineScope.launch {
            val workoutsNavigationList = workoutsNavigationService.getWorkoutsNavigationList()

            setState {
                this.workoutsNavigationList = workoutsNavigationList
            }
        }
    }

    override fun RBuilder.render() {
        val workoutsNavigationService = WorkoutsNavigationService(coroutineContext)
        state.workoutsNavigationList?.let { workoutsNavigationList ->
            route<SmallNavigationProps>("/workouts/:selectedLineLink") { selectedLineLink ->
                child(SmallNavigation::class) {
                    attrs.navLines = workoutsNavigationList
                    attrs.selectedLineLink = selectedLineLink.match.params.selectedLineLink
                    attrs.deleteFunction = { id: Int ->
                        props.coroutineScope.launch {
                            workoutsNavigationService.deleteWorkoutsSection(id)
                        }
                    }
                    attrs.editFunction = { id: Int, listOfInputValues: List<String> ->
                        props.coroutineScope.launch {
                            workoutsNavigationService.editWorkoutsSection(
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
                            workoutsNavigationService.addWorkoutsSection(
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