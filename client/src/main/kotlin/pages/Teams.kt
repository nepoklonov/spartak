package pages

import headerText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import kotlinx.html.js.onSubmitFunction
import model.NavigationDTO
import model.TeamDTO
import model.TeamMemberDTO
import model.TrainerDTO
import pageComponents.*
import react.*
import react.router.dom.route
import services.TeamService
import services.TrainerService
import smallHeaderText
import styled.*

//data class TeamsNavigation(val year: String) {
//    val header = "Команда $year"
//    val link = year
//}
//
//val teamsNavigationList = listOf(
//    TeamsNavigation("2003"),
//    TeamsNavigation("2004"),
//    TeamsNavigation("2006")
//)

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
    var trainerInputs: MutableMap<String, Input> = mutableMapOf(
        "name" to Input("ФИО", "name"),
        "dateOfBirth" to Input("Дата рождения", "dateOfBirth"),
        "info" to Input("Информация", "info"),
    )
    var teamMemberInputs: MutableMap<String, Input> = mutableMapOf(
        "firstName" to Input("Имя", "firstName"),
        "lastName" to Input("Фамилия", "lastName"),
        "role" to Input(
            "Амплуа", "role", isSelect = true,
            options = mapOf("defenders" to "Защитники", "strikers" to "Нападающие", "goalkeepers" to "Вратари"),
            allowOtherOption = true
        ),
        "birthday" to Input("Дата рождения", "birthday"),
        "city" to Input("Город", "city"),
    )
    var smallNavigationForm: Boolean = false
    var addTrainerForm: Boolean = false
    var addTeamMemberForm: Boolean = false
    var editSmallNavigationForm: NavigationDTO? = null
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
                    if (team.id != null) {
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
                        teamService.getTeamMemberByRoleAndTeam(role.key, team.link)
                    } catch (e: Throwable) {
                        console.log(e)
                        setState {
                            error = e
                        }
                        return@launch
                    }
                    console.log(teamMembers)
                    teamMembersWithRoles[role.value] = teamMembers
                    console.log(teamMembersWithRoles)

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
        styledDiv {
            css {
                overflow = Overflow.hidden
            }

            headerText {
                +"Команды"
            }

            styledDiv {
                css {
                    float = Float.left
                    backgroundColor = Color.white
                    textDecoration = TextDecoration.none
                }
                if (state.navigationList != null) {
                    state.navigationList!!.forEach { teamsNavigation ->
                        route<SmallNavigationProps>("/teams/:selectedLink") { linkProps ->
                            child(SmallNavigation::class) {
                                attrs.string = teamsNavigation.header
                                attrs.link = teamsNavigation.link
                                attrs.selectedLink = linkProps.match.params.selectedLink
                            }
                        }
                        child(DeleteButtonComponent::class) {
                            attrs.updateState = {
                                val teamService = TeamService(coroutineContext)
                                props.coroutineScope.launch {
                                    teamService.deleteTeam(teamsNavigation.id!!)
                                }
                            }
                        }
                        if (state.editSmallNavigationForm != teamsNavigation) {
                            child(EditButtonComponent::class) {
                                attrs.updateState = {
                                    setState {
                                        editSmallNavigationForm = teamsNavigation
                                    }
                                }
                            }
                        } else {
                            child(SmallNavigationForm::class) {
                                attrs.inputValues = listOf(teamsNavigation.header, teamsNavigation.link)
                                attrs.addSection = { listOfInputValues ->
                                    val teamService = TeamService(coroutineContext)
                                    props.coroutineScope.launch {
                                        teamService.editTeam(
                                            TeamDTO(
                                                teamsNavigation.id,
                                                listOfInputValues[0],
                                                listOfInputValues[1],
                                                true,
                                                props.selectedTeam,
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (!state.smallNavigationForm) {
                        child(AddButtonComponent::class) {
                            attrs.updateState = {
                                setState {
                                    smallNavigationForm = true
                                }
                            }
                        }
                    } else {
                        child(SmallNavigationForm::class) {
                            attrs.inputValues = listOf("", "")
                            attrs.addSection = { listOfInputValues ->
                                val teamService = TeamService(coroutineContext)
                                props.coroutineScope.launch {
                                    teamService.addTeam(
                                        TeamDTO(
                                            null,
                                            listOfInputValues[0],
                                            listOfInputValues[1],
                                            true,
                                            props.selectedTeam,
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

            val error = state.error
            if (error != null) {
                throw error
            }
            styledDiv {
                css {
                    overflow = Overflow.hidden
                }
                smallHeaderText {
                    +"Тренер"
                }
                if (state.trainer != null) {
                    state.trainer.let { trainer ->
                        styledImg(src = "/images/" + trainer!!.photo) {
                            css {
                                float = Float.left
                            }
                        }
                        styledDiv {
                            css {
                                textAlign = TextAlign.center
                            }
                            smallHeaderText {
                                +trainer.name
                            }
                            +trainer.info
                        }

                        child(DeleteButtonComponent::class) {
                            attrs.updateState = {
                                val trainerService = TrainerService(coroutineContext)
                                props.coroutineScope.launch {
                                    trainerService.deleteTrainer(trainer.id!!)
                                }
                            }
                        }
                        if (state.editTrainerForm != trainer) {
                            child(EditButtonComponent::class) {
                                attrs.updateState = {
                                    setState {
                                        editTrainerForm = trainer
                                        state.trainerInputs["name"]!!.inputValue = trainer.name
                                        state.trainerInputs["dateOfBirth"]!!.inputValue = trainer.dateOfBirth
                                        state.trainerInputs["info"]!!.inputValue = trainer.info
                                    }
                                }
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
                                            if (it.inputValue == "") {
                                                setState {
                                                    it.isRed = true
                                                }
                                                formIsCompleted = false
                                            }
                                        }
                                        if (formIsCompleted) {
                                            trainerService.editTrainer(
                                                TrainerDTO(
                                                    trainer.id,
                                                    props.selectedTeam,
                                                    "address.png",
                                                    state.trainerInputs["name"]!!.inputValue,
                                                    state.trainerInputs["dateOfBirth"]!!.inputValue,
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
                } else {
                    +"Загрузка..."
                }
            }
            if (state.teamMembersWithRoles != null) {
                state.teamMembersWithRoles!!.forEach() { teamMembers ->
                    smallHeaderText {
                        +teamMembers.key
                    }
                    teamMembers.value.forEach { teamMember ->
                        styledImg(src = "/images/" + teamMember.photo) { }

                        child(DeleteButtonComponent::class) {
                            attrs.updateState = {
                                val teamService = TeamService(coroutineContext)
                                props.coroutineScope.launch {
                                    teamService.deleteTeamMember(teamMember.id!!)
                                }
                            }
                        }

                        if (state.editTeamMemberForm != teamMember) {
                            child(EditButtonComponent::class) {
                                attrs.updateState = {
                                    setState {
                                        editTeamMemberForm = teamMember
                                        teamMemberInputs["firstName"]!!.inputValue = teamMember.firstName
                                        teamMemberInputs["lastName"]!!.inputValue = teamMember.lastName
                                        teamMemberInputs["role"]!!.inputValue = teamMember.role
                                        teamMemberInputs["birthday"]!!.inputValue = teamMember.birthday
                                        teamMemberInputs["city"]!!.inputValue = teamMember.city
                                    }
                                }
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
                                            if (it.inputValue == "") {
                                                setState {
                                                    it.isRed = true
                                                }
                                                formIsCompleted = false
                                            }
                                        }
                                        if (formIsCompleted) {
                                            teamService.editTeamMember(
                                                TeamMemberDTO(
                                                    teamMember.id,
                                                    props.selectedTeam,
                                                    "address.png",
                                                    state.teamMemberInputs["firstName"]!!.inputValue,
                                                    state.teamMemberInputs["lastName"]!!.inputValue,
                                                    state.teamMemberInputs["role"]!!.inputValue,
                                                    state.teamMemberInputs["birthday"]!!.inputValue,
                                                    state.teamMemberInputs["city"]!!.inputValue,
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
            } else {
                +"Загрузка..."
            }
        }

        if (!state.addTrainerForm) {
            child(AddButtonComponent::class) {
                attrs.updateState = {
                    setState {
                        addTrainerForm = true
                    }
                }
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
                            if (it.inputValue == "") {
                                setState {
                                    it.isRed = true
                                }
                                formIsCompleted = false
                            }
                        }
                        if (formIsCompleted) {
                            trainerService.addTrainer(
                                TrainerDTO(
                                    null,
                                    props.selectedTeam,
                                    "address.png",
                                    state.trainerInputs["name"]!!.inputValue,
                                    state.trainerInputs["dateOfBirth"]!!.inputValue,
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

        if (!state.addTeamMemberForm) {
            child(AddButtonComponent::class) {
                attrs.updateState = {
                    setState {
                        addTeamMemberForm = true
                    }
                }
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
                            if (it.inputValue == "") {
                                setState {
                                    it.isRed = true
                                }
                                formIsCompleted = false
                            }
                        }
                        if (formIsCompleted) {
                            teamService.addTeamMember(
                                TeamMemberDTO(
                                    null,
                                    props.selectedTeam,
                                    "address.png",
                                    state.teamMemberInputs["firstName"]!!.inputValue,
                                    state.teamMemberInputs["lastName"]!!.inputValue,
                                    state.teamMemberInputs["role"]!!.inputValue,
                                    state.teamMemberInputs["birthday"]!!.inputValue,
                                    state.teamMemberInputs["city"]!!.inputValue,
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