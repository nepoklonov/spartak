package pages

import content
import grid
import header
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.js.onSubmitFunction
import model.NavigationDTO
import model.WorkoutDTO
import navigation
import pageComponents.*
import react.*
import react.router.dom.route
import services.WorkoutsNavigationService
import services.WorkoutsService
import styled.*
import tableContent
import tableHeader
import kotlin.js.Date

val months = mapOf(
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

external interface WorkoutsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedWorkout: String
}

class WorkoutsState : RState {
    var error: Throwable? = null
    var workoutsNavigationList: List<NavigationDTO>? = null
    var workouts: List<WorkoutDTO>? = null
    var inputs: MutableMap<String, Input> = mutableMapOf(
        "startDatetime" to Input("Начало тренировки", "startDatetime", isDateTime = true),
        "endDatetime" to Input("Конец", "endDatetime", isDateTime = true),
        "text" to Input("Текст", "text"),
    )
    var smallNavigationForm: Boolean = false
    var addWorkoutForm: Boolean = false
    var editSmallNavigationForm: NavigationDTO? = null
    var editWorkoutForm: WorkoutDTO? = null
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
} else {
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
    init {
        state = WorkoutsState()
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val timetableService = WorkoutsService(coroutineContext)
        val workoutsNavigationService = WorkoutsNavigationService(coroutineContext)
        props.coroutineScope.launch {
            val workoutsNavigationList = try {
                workoutsNavigationService.getWorkoutsNavigationList()
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }

            val listOfWorkouts = try {
                timetableService.getWeekWorkoutsBySection(monday, sunday, props.selectedWorkout)
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }

            setState {
                this.workouts = listOfWorkouts
                this.workoutsNavigationList = workoutsNavigationList
            }
        }
    }

    override fun RBuilder.render() {

        grid {
            navigation {
                if (state.workoutsNavigationList != null) {
                    state.workoutsNavigationList!!.forEach { workoutsNavigation ->
                        route<SmallNavigationProps>("/workouts/:selectedLink") { linkProps ->
                            child(SmallNavigation::class) {
                                attrs.string = workoutsNavigation.header
                                attrs.link = workoutsNavigation.link
                                attrs.selectedLink = linkProps.match.params.selectedLink
                            }
                        }
                        if (document.cookie.contains("role=admin")) {
                            child(AdminButtonComponent::class) {
                                attrs.updateState = {
                                    val workoutsNavigationService = WorkoutsNavigationService(coroutineContext)
                                    props.coroutineScope.launch {
                                        workoutsNavigationService.deleteWorkoutsSection(workoutsNavigation.id!!)
                                    }
                                }
                                attrs.type = "delete"
                            }
                            if (state.editSmallNavigationForm != workoutsNavigation) {
                                child(AdminButtonComponent::class) {
                                    attrs.updateState = {
                                        setState {
                                            editSmallNavigationForm = workoutsNavigation
                                        }
                                    }
                                    attrs.type = "edit"
                                }
                            } else {
                                child(SmallNavigationForm::class) {
                                    attrs.inputValues = listOf(workoutsNavigation.header, workoutsNavigation.link)
                                    attrs.addSection = { listOfInputValues ->
                                        val workoutsNavigationService = WorkoutsNavigationService(coroutineContext)
                                        props.coroutineScope.launch {
                                            workoutsNavigationService.editWorkoutsSection(
                                                NavigationDTO(
                                                    workoutsNavigation.id,
                                                    listOfInputValues[0],
                                                    listOfInputValues[1]
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (document.cookie.contains("role=admin")) {
                        if (!state.smallNavigationForm) {
                            child(AdminButtonComponent::class) {
                                attrs.updateState = {
                                    setState {
                                        smallNavigationForm = true
                                    }
                                }
                                attrs.type = "add"
                            }
                        } else {
                            child(SmallNavigationForm::class) {
                                attrs.inputValues = listOf("", "")
                                attrs.addSection = { listOfInputValues ->
                                    val workoutsNavigationService = WorkoutsNavigationService(coroutineContext)
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
                    }
                } else {
                    +"Загрузка..."
                }
            }

            header {
                +"Расписание тренировок"
            }


            content {
                styledDiv {
                    daysOfWeek.forEach { daysOfWeek ->
                        styledDiv {
                            css {
                                marginRight = 32.px
                                marginLeft = 32.px
                            }

                            tableHeader {
                                +daysOfWeek.value
                                +Date(monday + daysOfWeek.key * msInDay).getDate().toString()
                                +(months[Date(monday + daysOfWeek.key * msInDay).getMonth()] ?: error(""))
                            }
                            if (state.workouts != null) {
                                state.workouts!!.forEach { workout ->
                                    console.log(workout)
                                    if (daysOfWeek.key % 7 == workout.dayOfWeek) {
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
                                            styledTd {
                                                if (document.cookie.contains("role=admin")) {
                                                    child(AdminButtonComponent::class) {
                                                        attrs.updateState = {
                                                            val timetableService = WorkoutsService(coroutineContext)
                                                            props.coroutineScope.launch {
                                                                timetableService.makeNotActual(workout.id!!, monday)
                                                            }
                                                        }
                                                        attrs.type = "delete"
                                                    }
                                                    if (state.editWorkoutForm != workout) {
                                                        child(AdminButtonComponent::class) {
                                                            attrs.updateState = {
                                                                setState {
                                                                    editWorkoutForm = workout
                                                                }
                                                            }
                                                            attrs.type = "edit"
                                                        }
                                                    } else {
                                                        styledForm {
                                                            attrs.onSubmitFunction = { event ->
                                                                event.preventDefault()
                                                                event.stopPropagation()
                                                                val timetableService = WorkoutsService(coroutineContext)
                                                                props.coroutineScope.launch {
                                                                    var formIsCompleted = true
                                                                    state.inputs.values.forEach {
                                                                        if (it.isRed) {
                                                                            formIsCompleted = false
                                                                        }
                                                                    }
                                                                    if (formIsCompleted) {
                                                                        timetableService.makeNotActual(
                                                                            workout.id!!,
                                                                            monday
                                                                        )
                                                                        timetableService.addWorkout(
                                                                            WorkoutDTO(
                                                                                null,
                                                                                Date(state.inputs["startDatetime"]!!.inputValue).getHours()
                                                                                    .toString()
                                                                                        + ":"
                                                                                        + Date(state.inputs["startDatetime"]!!.inputValue).getMinutes()
                                                                                    .toString(),
                                                                                Date(state.inputs["endDatetime"]!!.inputValue).getHours()
                                                                                    .toString()
                                                                                        + ":"
                                                                                        + Date(state.inputs["endDatetime"]!!.inputValue).getMinutes()
                                                                                    .toString(),
                                                                                Date(state.inputs["startDatetime"]!!.inputValue).getDay(),
                                                                                props.selectedWorkout,
                                                                                state.inputs["text"]!!.inputValue,
                                                                                monday,
                                                                                9999999999999.0
                                                                            )
                                                                        )
                                                                    }
                                                                }
                                                            }
                                                            child(FormViewComponent::class) {
                                                                attrs.inputs = state.inputs
                                                                attrs.updateState =
                                                                    { key: String, value: String, isRed: Boolean ->
                                                                        setState {
                                                                            state.inputs[key]!!.inputValue = value
                                                                            state.inputs[key]!!.isRed = isRed
                                                                        }
                                                                    }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (document.cookie.contains("role=admin")) {

                    styledDiv {
                        if (!state.addWorkoutForm) {
                            child(AdminButtonComponent::class) {
                                attrs.updateState = {
                                    setState {
                                        addWorkoutForm = true
                                    }
                                }
                                attrs.type = "add"
                            }
                        } else {
                            styledForm {
                                attrs.onSubmitFunction = { event ->
                                    event.preventDefault()
                                    event.stopPropagation()
                                    val timetableService = WorkoutsService(coroutineContext)
                                    props.coroutineScope.launch {
                                        var formIsCompleted = true
                                        state.inputs.values.forEach {
                                            if (it.isRed) {
                                                formIsCompleted = false
                                            }
                                        }
                                        if (formIsCompleted) {
                                            timetableService.addWorkout(
                                                WorkoutDTO(
                                                    null,
                                                    Date(state.inputs["startDatetime"]!!.inputValue).getHours()
                                                        .toString()
                                                            + ":"
                                                            + Date(state.inputs["startDatetime"]!!.inputValue).getMinutes()
                                                        .toString(),
                                                    Date(state.inputs["endDatetime"]!!.inputValue).getHours().toString()
                                                            + ":"
                                                            + Date(state.inputs["endDatetime"]!!.inputValue).getMinutes()
                                                        .toString(),
                                                    Date(state.inputs["startDatetime"]!!.inputValue).getDay(),
                                                    props.selectedWorkout,
                                                    state.inputs["text"]!!.inputValue,
                                                    monday,
                                                    9999999999999.0
                                                )
                                            )
                                        }
                                    }
                                }
                                child(FormViewComponent::class) {
                                    attrs.inputs = state.inputs
                                    attrs.updateState = { key: String, value: String, isRed: Boolean ->
                                        setState {
                                            state.inputs[key]!!.inputValue = value
                                            state.inputs[key]!!.isRed = isRed
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

