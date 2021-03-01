package pages.Workouts

import Consts.*
import pages.WorkoutsAdminFunctions.AddForm
import pages.WorkoutsAdminFunctions.AdminTd
import adminPageComponents.Input
import content
import grid
import header
import isAdmin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.marginLeft
import kotlinx.css.marginRight
import kotlinx.css.px
import kotlinx.css.width
import model.WorkoutDTO
import navigation
import react.*
import services.WorkoutsService
import styled.css
import styled.styledDiv
import styled.styledTd
import tableContent
import tableHeader
import kotlin.js.Date

external interface WorkoutsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedWorkout: String
}

//TODO: remove duplicate fragments
class WorkoutsState : RState {
    var workouts: List<WorkoutDTO>? = null
    var inputs: MutableMap<String, Input> = workoutsInputs
}

class Workouts : RComponent<WorkoutsProps, WorkoutsState>() {
    init {
        state = WorkoutsState()
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val timetableService = WorkoutsService(coroutineContext)
        props.coroutineScope.launch {
            val listOfWorkouts = timetableService.getWeekWorkoutsBySection(monday, sunday, props.selectedWorkout)

            setState {
                this.workouts = listOfWorkouts
            }
        }
    }

    override fun RBuilder.render() {

        grid {
            header {
                +"Расписание тренировок"
            }

            navigation {
                child(WorkoutsNavigation::class){
                    attrs.coroutineScope = props.coroutineScope
                }
            }

            content {
                styledDiv {
                    daysOfWeek.forEach { dayOfWeek ->
                        dailyTimetable(dayOfWeek.key, dayOfWeek.value)
                    }
                }
                if (isAdmin) {
                    child(AddForm::class){
                        attrs.coroutineScope = props.coroutineScope
                        attrs.selectedWorkout = props.selectedWorkout
                    }
                }
            }
        }
    }

    private fun RBuilder.dailyTimetable(dayNumber: Int, header: String) {

        styledDiv {
            css {
                marginRight = 32.px
                marginLeft = 32.px
            }

            tableHeader {
                +header
                +", "
                +Date(monday + dayNumber * msInDay).getDate().toString()
                +" "
                +(months[Date(monday + dayNumber * msInDay).getMonth()] ?: error(""))
            }

            state.workouts?.forEach { workout ->

                if (dayNumber % 7 == workout.dayOfWeek) {
                    tableContent {
                        styledTd {
                            css {
                                width = 400.px
                            }
                            +workout.startTime
                            +" - "
                            +workout.endTime
                        }
                        styledTd {
                            +workout.text
                        }
                        child(AdminTd::class){
                            attrs.coroutineScope = props.coroutineScope
                            attrs.selectedWorkout = props.selectedWorkout
                            attrs.workout = workout
                        }
                    }
                }
            } ?: run { +"Загрузка..." }
        }
    }
}


