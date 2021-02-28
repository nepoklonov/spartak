package pages

import adminPageComponents.*
import content
import grid
import header
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.js.onSubmitFunction
import model.NavigationDTO
import model.TeamDTO
import model.TeamMemberDTO
import model.TrainerDTO
import navigation
import pageComponents.*
import react.*
import react.router.dom.route
import services.TeamService
import services.TrainerService
import smallHeaderText
import structure.SmallNavigation
import structure.SmallNavigationProps
import styled.*

val roleMap = mapOf("defenders" to "Защитники", "strikers" to "Нападающие", "goalkeepers" to "Вратари")

external interface TeamsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedTeam: String
}

class TeamsState : RState {
    var error: Throwable? = null
    var navigationList: List<NavigationDTO>? = null
    var team: TeamDTO? = null
    var trainer: TrainerDTO? = null
    var teamMembersWithRoles: Map<String, List<TeamMemberDTO>>? = null
    var teamInputs: MutableMap<String, Input> = mutableMapOf(
        "teamName" to Input("Название команды", "teamName"),
        "teamLink" to Input("Ссылка", "teamlink"),
        "year" to Input("Год рождения участников", "year"),
        "name" to Input("ФИО тренера", "name"),
        "info" to Input("Информация о тренере", "info", isNecessary = false),
    )
    var trainerInputs: MutableMap<String, Input> = mutableMapOf(
        "name" to Input("ФИО", "name"),
        "info" to Input("Информация", "info"),
    )
    var teamMemberInputs: MutableMap<String, Input> = mutableMapOf(
        "number" to Input("Номер", "number"),
        "firstName" to Input("Имя", "firstName"),
        "lastName" to Input("Фамилия", "lastName"),
        "role" to Input(
            "Амплуа", "role", isSelect = true,
            options = mapOf("defenders" to "Защитники", "strikers" to "Нападающие", "goalkeepers" to "Вратари"),
            allowOtherOption = true
        ),
        "birthday" to Input("Дата рождения", "birthday", isNecessary = false),
        "city" to Input("Город", "city", isNecessary = false),
        "teamRole" to Input(
            "к/а",
            "teamRole",
            isSelect = true,
            options = mapOf("к" to "к", "а" to "а", "null" to "нет")
        )
    )
    var addTrainerForm: Boolean = false
    var addTeamMemberForm: Boolean = false
    var editTrainerForm: TrainerDTO? = null
    var editTeamMemberForm: TeamMemberDTO? = null
}

class Teams : RComponent<TeamsProps, TeamsState>() {
    init {
        state = TeamsState()
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    private fun getState(selectedTeam: String, coroutineScope: CoroutineScope) {
        val trainerService = TrainerService(coroutineContext)
        val teamService = TeamService(coroutineContext)

        coroutineScope.launch {
            val navigationList = try {
                teamService.getNavigationList()
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }
            val team = try {
                teamService.getTeamByLink(selectedTeam)
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }
            if (team.id != null) {
                val trainer = try {
                    if (team.link != null) {
                        trainerService.getTrainerByLink(team.link!!)
                    } else {
                        null
                    }
                } catch (e: Throwable) {
                    setState {
                        error = e
                    }
                    return@launch
                }

                val teamMembersWithRoles = mutableMapOf<String, List<TeamMemberDTO>>()

                roleMap.forEach { role ->
                    val teamMembers = try {
                        teamService.getTeamMemberByRoleAndTeam(role.key, team.link!!)
                    } catch (e: Throwable) {
                        console.log(e)
                        setState {
                            error = e
                        }
                        return@launch
                    }
                    teamMembersWithRoles[role.value] = teamMembers
                }
                setState {
                    this.team = team
                    this.navigationList = navigationList
                    this.teamMembersWithRoles = teamMembersWithRoles
                    this.trainer = trainer
                }
            }
        }
    }

    override fun componentDidMount() {
        getState(props.selectedTeam, props.coroutineScope)
    }

    override fun componentDidUpdate(prevProps: TeamsProps, prevState: TeamsState, snapshot: Any) {
        if (this.props.selectedTeam != prevProps.selectedTeam) {
            getState(props.selectedTeam, props.coroutineScope)
        }
    }

    override fun RBuilder.render() {
        grid {
            navigation {
                val teamService = TeamService(coroutineContext)

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
//                                props.coroutineScope.launch {
//                                    teamService.addTeam(
//                                        TeamDTO(
//                                            null,
//                                            listOfInputValues[0],
//                                            listOfInputValues[1],
//                                            true,
//
//                                        )
//                                    )
//                                trainerService.addTrainer(
//                                    TrainerDTO(
//                                        null,
//                                        state.teamInputs["teamLink"]!!.inputValue,
//                                        "address.png",
//                                        state.teamInputs["name"]!!.inputValue,
//                                        state.teamInputs["info"]!!.inputValue,
//                                    )
//                                )
//                                }
                            }
                        }
                    }
                } ?: run {
                    +"Загрузка..."
                }
            }

            header {
                +"Команды"
            }

            content {

                val error = state.error
                if (error != null) {
                    throw error
                }
                styledDiv {
                    css {
                        overflow = Overflow.hidden
                    }

                    if (state.trainer != null) {
                        console.log(state.trainer)
                        state.trainer.let { trainer ->
                            styledDiv {
                                css {
                                    display = Display.flex
                                    justifyContent = JustifyContent.spaceBetween
                                }
                                child(RedFrameComponent::class) {
                                    attrs.isTrainer = true
                                    attrs.trainer = trainer.also { console.log(it) }
                                }
                                styledDiv {
                                    css {
                                        width = 100.pct
                                        textAlign = TextAlign.center
                                        justifyContent = JustifyContent.center
                                        display = Display.flex
                                        height = 100.pct
                                        padding = 50.px.toString()
                                    }
                                    +trainer?.info!!
                                }

                            }
                            if (document.cookie.contains("role=admin")) {
                                child(AdminButtonComponent::class) {
                                    attrs.updateState = {
                                        val trainerService = TrainerService(coroutineContext)
                                        props.coroutineScope.launch {
                                            trainerService.deleteTrainer(trainer!!.id!!)
                                        }
                                    }
                                    attrs.button = AdminButtonType.Delete
                                }
                                if (state.editTrainerForm != trainer) {
                                    child(AdminButtonComponent::class) {
                                        attrs.updateState = {
                                            setState {
                                                editTrainerForm = trainer
                                                state.trainerInputs["name"]!!.inputValue = trainer!!.name
                                                state.trainerInputs["info"]!!.inputValue = trainer.info
                                            }
                                        }
                                        attrs.button = AdminButtonType.Edit
                                    }
                                } else {
                                    styledForm {
                                        attrs.onSubmitFunction = { event ->
                                            event.preventDefault()
                                            event.stopPropagation()
                                            val trainerService = TrainerService(coroutineContext)
                                            props.coroutineScope.launch {
                                                var formIsCompleted = true
                                                state.trainerInputs.values.forEach {
                                                    if (it.isRed) {
                                                        formIsCompleted = false
                                                    }
                                                }
                                                if (formIsCompleted) {
                                                    trainerService.editTrainer(
                                                        TrainerDTO(
                                                            trainer!!.id,
                                                            props.selectedTeam,
                                                            "address.png",
                                                            state.trainerInputs["name"]!!.inputValue,
                                                            state.trainerInputs["info"]!!.inputValue,
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                        child(FormViewComponent::class) {
                                            attrs.inputs = state.trainerInputs
                                            attrs.updateState = { key: String, value: String, isRed: Boolean ->
                                                setState {
                                                    state.trainerInputs[key]!!.inputValue = value
                                                    state.trainerInputs[key]!!.isRed = isRed
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        +"Загрузка..."
                    }
                }
                if (state.teamMembersWithRoles != null) {
                    state.teamMembersWithRoles!!.forEach() { teamMembers ->
                        smallHeaderText {
                            +teamMembers.key
                        }
                        styledDiv {
                            css {
                                display = Display.flex
                                flexWrap = FlexWrap.wrap
                            }

                            teamMembers.value.forEach { teamMember ->
                                child(RedFrameComponent::class) {
                                    attrs.isTrainer = false
                                    attrs.teamMember = teamMember
                                }
                                //TODO: вынести в отдельную функцию
                                if (document.cookie.contains("role=Admin")) {
                                    child(AdminButtonComponent::class) {
                                        attrs.updateState = {
                                            val teamService = TeamService(coroutineContext)
                                            props.coroutineScope.launch {
                                                teamService.deleteTeamMember(teamMember.id!!)
                                            }
                                        }
                                        attrs.button = AdminButtonType.Delete
                                    }

                                    if (state.editTeamMemberForm != teamMember) {
                                        child(AdminButtonComponent::class) {
                                            attrs.updateState = {
                                                setState {
                                                    editTeamMemberForm = teamMember
                                                    teamMemberInputs["number"]!!.inputValue = teamMember.number
                                                    teamMemberInputs["firstName"]!!.inputValue =
                                                        teamMember.firstName
                                                    teamMemberInputs["lastName"]!!.inputValue = teamMember.lastName
                                                    teamMemberInputs["role"]!!.inputValue = teamMember.role
                                                    teamMemberInputs["birthday"]!!.inputValue = teamMember.birthday
                                                    teamMemberInputs["city"]!!.inputValue = teamMember.city
                                                    teamMemberInputs["teamRole"]!!.inputValue = teamMember.number
                                                }
                                            }
                                            attrs.button = AdminButtonType.Edit
                                        }
                                    } else {
                                        styledForm {
                                            attrs.onSubmitFunction = { event ->
                                                console.log(state.teamMemberInputs)
                                                event.preventDefault()
                                                event.stopPropagation()
                                                val teamService = TeamService(coroutineContext)
                                                props.coroutineScope.launch {
                                                    var formIsCompleted = true
                                                    state.teamMemberInputs.values.forEach {
                                                        if (it.isRed) {
                                                            formIsCompleted = false
                                                        }
                                                    }
                                                    if (formIsCompleted) {
                                                        teamService.editTeamMember(
                                                            TeamMemberDTO(
                                                                teamMember.id,
                                                                props.selectedTeam,
                                                                state.teamMemberInputs["number"]!!.inputValue,
                                                                "address.png",
                                                                state.teamMemberInputs["firstName"]!!.inputValue,
                                                                state.teamMemberInputs["lastName"]!!.inputValue,
                                                                state.teamMemberInputs["role"]!!.inputValue,
                                                                state.teamMemberInputs["birthday"]!!.inputValue,
                                                                state.teamMemberInputs["city"]!!.inputValue,
                                                                state.teamMemberInputs["teamRole"]!!.inputValue
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                            child(FormViewComponent::class) {
                                                attrs.inputs = state.teamMemberInputs
                                                attrs.updateState = { key: String, value: String, isRed: Boolean ->
                                                    setState {
                                                        state.teamMemberInputs[key]!!.inputValue = value
                                                        state.teamMemberInputs[key]!!.isRed = isRed
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    +"Загрузка..."
                }
                if (document.cookie.contains("role=admin")) {
                    if (!state.addTeamMemberForm) {
                        child(AdminButtonComponent::class) {
                            attrs.updateState = {
                                setState {
                                    addTeamMemberForm = true
                                }
                            }
                            attrs.button = AdminButtonType.Add
                        }
                    } else {
                        styledForm {
                            attrs.onSubmitFunction = { event ->
                                console.log(state.teamMemberInputs)
                                event.preventDefault()
                                event.stopPropagation()
                                val teamService = TeamService(coroutineContext)
                                props.coroutineScope.launch {
                                    var formIsCompleted = true
                                    state.teamMemberInputs.values.forEach {
                                        if (it.isRed) {
                                            formIsCompleted = false
                                        }
                                    }
                                    if (formIsCompleted) {
                                        teamService.addTeamMember(
                                            TeamMemberDTO(
                                                null,
                                                props.selectedTeam,
                                                state.teamMemberInputs["number"]!!.inputValue,
                                                "address.png",
                                                state.teamMemberInputs["firstName"]!!.inputValue,
                                                state.teamMemberInputs["lastName"]!!.inputValue,
                                                state.teamMemberInputs["role"]!!.inputValue,
                                                state.teamMemberInputs["birthday"]!!.inputValue,
                                                state.teamMemberInputs["city"]!!.inputValue,
                                                state.teamMemberInputs["teamRole"]!!.inputValue
                                            )
                                        )
                                    }
                                }
                            }
                            child(FormViewComponent::class) {
                                attrs.inputs = state.teamMemberInputs
                                attrs.updateState = { key: String, value: String, isRed: Boolean ->
                                    setState {
                                        state.teamMemberInputs[key]!!.inputValue = value
                                        state.teamMemberInputs[key]!!.isRed = isRed
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
//TODO: в этом файле чёрт ногу сломит
// , я тоже сломил.
// Функции админские нужно отделить от обычных.
// И провести большой рефакторинг.