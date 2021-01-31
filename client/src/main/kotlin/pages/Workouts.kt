package pages

import headerText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import react.*
import react.router.dom.navLink
import services.TimetableService
import styled.css
import styled.styledDiv
import tableHeader
import view.SmallNavigation
import kotlin.js.Date

val monthes = mapOf(
    0 to " января",
    1 to " февраля",
    2 to " марта",
    3 to " апреля",
    4 to " мая",
    5 to " июня",
    6 to " июля",
    7 to " августа",
    8 to " сентября",
    9 to " октября",
    10 to " ноября",
    11 to " декабря",
)

val daysOfWeek = mapOf(
    1 to "понедельник, ",
    2 to "вторник, ",
    3 to "среда, ",
    4 to "четверг, ",
    5 to "пятница, ",
    6 to "суббота, ",
    7 to "воскресенье, "
)

data class WorkoutsNavigation(val header: String, val link: String) {}

val workoutsNavigationList = listOf(
    WorkoutsNavigation("ШХМ", "/workouts/shhm"),
    WorkoutsNavigation("Спартак 2013", "/workouts/2013"),
    WorkoutsNavigation("Спартак 2003-2004", "/workouts/2003"),
    WorkoutsNavigation("Спартак 2005", "/workouts/2005"),
    WorkoutsNavigation("Вратарские Тренировки", "/workouts/goalkeepers"),
    WorkoutsNavigation("Группа набора", "/workouts/recruitment"),
    WorkoutsNavigation("Спартак 2006", "/workouts/2006"),
    WorkoutsNavigation("Спартак Красная Ракета", "/workouts/red"),
    WorkoutsNavigation("Спартак 2008", "/workouts/20008"),
)

data class WorkoutWithDate(
    val dayOfWeek: Int,
    val date: String,
    val time: String,
    val teamId: Int,
    val type: String,
    val place: String,
)

external interface WorkoutsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedWorkout: String
}

class WorkoutsState : RState {
    var error: Throwable? = null
    var workouts: List<WorkoutWithDate>? = null
}

const val msInDay = 1000 * 3600 * 24
val monday = if (Date().getDay() != 0) {
    Date(
        ((Date(
            Date().getFullYear(),
            Date().getMonth(),
            Date().getDate()
        )).getTime() - Date().getDay() * msInDay)
    ).getTime()
} else{
    Date(
        ((Date(
            Date().getFullYear(),
            Date().getMonth(),
            Date().getDate()
        )).getTime() - 7 * msInDay)
    ).getTime()
}

val sunday = monday + 7 * msInDay


class Workouts : RComponent<WorkoutsProps, WorkoutsState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val timetableService = TimetableService(coroutineContext)
        props.coroutineScope.launch {
            console.log(props.selectedWorkout)
            val listOfWorkouts = try {
                timetableService.getWeekTimetableByType(monday, sunday, props.selectedWorkout)
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }

            val listOfWorkoutsWithDate: MutableList<WorkoutWithDate> = mutableListOf()
            listOfWorkouts.forEach {
                listOfWorkoutsWithDate += WorkoutWithDate(
                    Date(it.datetime).getDay(),

                    Date(it.datetime).getDate().toString()
                            + "."
                            + Date(it.datetime).getMonth().toString()
                            + "."
                            + Date(it.datetime).getFullYear().toString(),

                    Date(it.datetime).getHours().toString()
                            + ":"
                            + Date(it.datetime).getMinutes().toString(),

                    it.teamId,
                    it.type,
                    it.place
                )
            }

            setState {
                this.workouts = listOfWorkoutsWithDate
            }
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                overflow = Overflow.hidden
            }

            headerText {
                +"Расписание тренировок"
            }
            styledDiv {
                css {
                    float = Float.left
                    backgroundColor = Color.white
                    textDecoration = TextDecoration.none
                }
                workoutsNavigationList.forEach {
                    navLink<WorkoutsProps>(to = it.link) {
                        child(SmallNavigation::class) {
                            attrs.selectedString = it.header
                        }
                    }
                }
            }

            styledDiv {

                daysOfWeek.forEach { daysOfWeek ->
                    tableHeader {
                        +daysOfWeek.value
                        +Date(monday + daysOfWeek.key * msInDay).getDate().toString()
                        +(monthes[Date(monday + daysOfWeek.key * msInDay).getMonth()] ?: error(""))
                    }
                    styledDiv {
                        css {
                            backgroundColor = Color.white
                        }
                        if (state.workouts != null) {
                            state.workouts!!.forEach { workout ->
                                if (daysOfWeek.key == workout.dayOfWeek % 7) {
                                    +workout.time
                                    +workout.place
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

