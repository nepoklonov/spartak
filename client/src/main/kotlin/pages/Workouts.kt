package pages

import headerText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.boxShadow
import kotlinx.html.js.onSubmitFunction
import model.NavigationDTO
import model.WorkoutDTO
import pageComponents.*
import react.*
import react.router.dom.route
import services.WorkoutsNavigationService
import services.WorkoutsService
import styled.*
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

data class WorkoutWithDate(
    val id: Int,
    val dayOfWeek: Int,
    val date: String,
    val time: String,
    val sectionLink: String,
    val text: String,
)

external interface WorkoutsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedWorkout: String
}

class WorkoutsState : RState {
    var error: Throwable? = null
    var workoutsNavigationList: List<NavigationDTO>? = null
    var workouts: List<WorkoutWithDate>? = null
    var inputs: MutableMap<String, Input> = mutableMapOf(
        "datetime" to Input("Дата и время", "datetime", isDateTime = true),
        "text" to Input("Текст", "text"),
    )
    var smallNavigationForm: Boolean = false
    var addWorkoutForm: Boolean = false
    var editSmallNavigationForm: NavigationDTO? = null
    var editWorkoutForm: WorkoutWithDate? = null
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

            val listOfWorkoutsWithDate: MutableList<WorkoutWithDate> = mutableListOf()
            listOfWorkouts.forEach {
                listOfWorkoutsWithDate += WorkoutWithDate(
                    it.id!!,
                    Date(it.datetime).getDay(),

                    Date(it.datetime).getDate().toString()
                            + "."
                            + Date(it.datetime).getMonth().toString()
                            + "."
                            + Date(it.datetime).getFullYear().toString(),

                    Date(it.datetime).getHours().toString()
                            + ":"
                            + Date(it.datetime).getMinutes().toString(),

                    it.sectionLink,
                    it.text
                )
            }

            setState {
                this.workouts = listOfWorkoutsWithDate
                this.workoutsNavigationList = workoutsNavigationList
            }
        }
    }

    override fun RBuilder.render() {

        styledDiv {
            css {
                display = Display.flex
                justifyContent = JustifyContent.spaceBetween
                alignItems = Align.flexStart
            }
            styledDiv {
                css {
                    marginTop = 115.px
                    backgroundColor = Color.white
                    boxShadow(color = rgba(0, 0, 0, 0.25), offsetX = 0.px, offsetY = 4.px, blurRadius = 4.px)
                }
                if (state.workoutsNavigationList != null) {
                    state.workoutsNavigationList!!.forEach { workoutsNavigation ->
                        route<SmallNavigationProps>("/workouts/:selectedLink") { linkProps ->
                            child(SmallNavigation::class) {
                                attrs.string = workoutsNavigation.header
                                attrs.link = workoutsNavigation.link
                                attrs.selectedLink = linkProps.match.params.selectedLink
                            }
                        }
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
                } else {
                    +"Загрузка..."
                }
            }

            styledDiv {
                css {
                    width = 100.pct
                    marginLeft = 50.px
                    marginRight = 50.px
                }
                styledDiv {
                    css {
                        width = 100.pct
                    }
                    headerText {
                        +"Расписание тренировок"
                    }
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

                            styledDiv {
                                css {
                                    marginTop = 3.px
                                    marginBottom = 3.px
                                    backgroundColor = Color.white
                                    boxShadow(
                                        color = rgba(0, 0, 0, 0.25),
                                        offsetX = 0.px,
                                        offsetY = 4.px,
                                        blurRadius = 4.px
                                    )
                                }
                                if (state.workouts != null) {
                                    state.workouts!!.forEach { workout ->
                                        if (daysOfWeek.key == workout.dayOfWeek % 7) {
                                            styledTr {
                                                css {
                                                    borderCollapse = BorderCollapse.collapse
                                                    borderSpacing = 3.px
                                                }
                                                styledTd {
                                                    css {
                                                        width = 100.px
                                                    }
                                                    +workout.time
                                                }
                                                styledTd {
                                                    +workout.text
                                                }
                                            }
                                        }
                                        child(AdminButtonComponent::class) {
                                            attrs.updateState = {
                                                val timetableService = WorkoutsService(coroutineContext)
                                                props.coroutineScope.launch {
                                                    timetableService.deleteWorkout(workout.id)
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
                                                            if (it.inputValue == "") {
                                                                setState {
                                                                    it.isRed = true
                                                                }
                                                                formIsCompleted = false
                                                            }
                                                        }
                                                        if (formIsCompleted) {
                                                            timetableService.editWorkout(
                                                                WorkoutDTO(
                                                                    workout.id,
                                                                    Date(state.inputs["date"]!!.inputValue).getTime(),
                                                                    props.selectedWorkout,
                                                                    state.inputs["text"]!!.inputValue,
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
                                val timemtableService = WorkoutsService(coroutineContext)
                                props.coroutineScope.launch {
                                    var formIsCompleted = true
                                    state.inputs.values.forEach {
                                        if (it.inputValue == "") {
                                            setState {
                                                it.isRed = true
                                            }
                                            formIsCompleted = false
                                        }
                                    }
                                    if (formIsCompleted) {
                                        timemtableService.addWorkout(
                                            WorkoutDTO(
                                                null,
                                                Date(state.inputs["datetime"]!!.inputValue).getTime(),
                                                props.selectedWorkout,
                                                state.inputs["text"]!!.inputValue,
                                            ).also { console.log(it) }
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

