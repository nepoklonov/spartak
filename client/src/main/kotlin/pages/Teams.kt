package pages

import headerText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import kotlinx.html.js.onSubmitFunction
import model.TeamDTO
import model.TeamMemberDTO
import model.TrainerDTO
import pageComponents.FormViewComponent
import pageComponents.Input
import pageComponents.SmallNavigation
import pageComponents.SmallNavigationProps
import react.*
import react.router.dom.route
import services.TeamService
import services.TrainerService
import smallHeaderText
import styled.*

data class TeamsNavigation(val year: String) {
    val header = "Команда $year"
    val link = year
}

val teamsNavigationList = listOf(
    TeamsNavigation("2003"),
    TeamsNavigation("2004"),
    TeamsNavigation("2006")
)

val roleList = listOf("Защитники", "Вратари", "Нападающие")


external interface TeamsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedTeam: String
}

class TeamsState : RState {
    var error: Throwable? = null
    var team: TeamDTO? = null
    var trainer: TrainerDTO? = null
    var teamMembersWithRoles: Map<String, List<TeamMemberDTO>>? = null
    var teamInputs: MutableList<Input> = mutableListOf(
        Input("Название", "name"),
        Input("Тип ээ че ладно", "type"),
        Input("id тренера", "trainerId"),
    )
    var trainerInputs: MutableList<Input> = mutableListOf(
        Input("ФИО", "name"),
        Input("Дата рождения", "dateOfBirth"),
        Input("Информация", "info"),
    )
    var teamMemberInputs: MutableList<Input> = mutableListOf(
        Input("Имя", "firstName"),
        Input("Фамилия", "lastName"),
        Input("Амплуа", "role"),
        Input("Дата рождения", "birthday"),
        Input("Город", "city"),
    )
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
            val team = try {
                teamService.getTeamByYear(selectedTeam)
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }
            if (team.id != null) {
                val trainer = try {
                    trainerService.getTrainerById(team.id!!)
                } catch (e: Throwable) {
                    setState {
                        error = e
                    }
                    return@launch
                }

                val teamMembersWithRoles = mutableMapOf<String, List<TeamMemberDTO>>()

                roleList.forEach { header ->
                    val teamMembers = try {
                        teamService.getTeamMemberByTeamIdAndRole(header, team.id!!)
                    } catch (e: Throwable) {
                        setState {
                            error = e
                        }
                        return@launch
                    }
                    teamMembersWithRoles[header] = teamMembers

                }


                setState {
                    this.team = team
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
                teamsNavigationList.forEach { teamsNavigationProps ->
                    route<SmallNavigationProps>("/teams/:selectedLink") { linkProps ->
                        child(SmallNavigation::class) {
                            attrs.string = teamsNavigationProps.header
                            attrs.link = teamsNavigationProps.link
                            attrs.selectedLink = linkProps.match.params.selectedLink
                        }
                    }
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
                    styledImg(src = "/images/" + state.trainer!!.photo) {
                        css {
                            float = Float.left
                        }
                    }
                    styledDiv {
                        css {
                            textAlign = TextAlign.center
                        }
                        smallHeaderText {
                            +(state.trainer?.name ?: "загрузка...")
                        }
                        +(state.trainer?.info ?: "загрузка...")
                    }

                }
            }
            if (state.teamMembersWithRoles != null) {
                state.teamMembersWithRoles!!.forEach() {
                    smallHeaderText {
                        +it.key
                    }
                    it.value.forEach {
                        styledImg(src = "/images/" + it.photo) { }
                    }
                }
            }
        }

        styledForm {
            attrs.onSubmitFunction = { event ->
                event.preventDefault()
                event.stopPropagation()
                val teamService =TeamService(coroutineContext)
                props.coroutineScope.launch {
                    var formIsCompleted = true
                    state.teamInputs.forEach {
                        if (it.inputValue == "") {
                            setState {
                                it.isRed = true
                            }
                            formIsCompleted = false
                        }
                    }
                    if (formIsCompleted) {
                        teamService.addTeam(
                            TeamDTO(
                                null,
                                state.teamInputs[0].inputValue,
                                true,
                                state.teamInputs[2].inputValue,
                                props.selectedTeam,
                                state.teamInputs[4].inputValue.toInt(),
                            )
                        )
                    }
                }
            }
            child(FormViewComponent::class) {
                attrs.inputs = state.teamInputs
                attrs.updateState = {i: Int, value: String, isRed: Boolean ->
                    setState {
                        state.teamInputs[i].inputValue = value
                        state.teamInputs[i].isRed = isRed
                    }
                }
            }
        }

        styledForm {
            attrs.onSubmitFunction = { event ->
                event.preventDefault()
                event.stopPropagation()
                val trainerService = TrainerService(coroutineContext)
                props.coroutineScope.launch {
                    var formIsCompleted = true
                    state.trainerInputs.forEach {
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
                                props.selectedTeam.toInt(),
                                "address.png",
                                state.trainerInputs[1].inputValue,
                                state.trainerInputs[2].inputValue,
                                state.trainerInputs[3].inputValue,
                            )
                        )
                    }
                }
            }
            child(FormViewComponent::class) {
                attrs.inputs = state.trainerInputs
                attrs.updateState = {i: Int, value: String, isRed: Boolean ->
                    setState {
                        state.trainerInputs[i].inputValue = value
                        state.trainerInputs[i].isRed = isRed
                    }
                }
            }
        }

        styledForm {
            attrs.onSubmitFunction = { event ->
                event.preventDefault()
                event.stopPropagation()
                val teamService = TeamService(coroutineContext)
                props.coroutineScope.launch {
                    var formIsCompleted = true
                    state.teamMemberInputs.forEach {
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
                                props.selectedTeam.toInt(),
                                "address.png",
                                state.teamMemberInputs[2].inputValue,
                                state.teamMemberInputs[3].inputValue,
                                state.teamMemberInputs[4].inputValue,
                                state.teamMemberInputs[5].inputValue,
                                state.teamMemberInputs[6].inputValue,
                            )
                        )
                    }
                }
            }
            child(FormViewComponent::class) {
                attrs.inputs = state.teamMemberInputs
                attrs.updateState = {i: Int, value: String, isRed: Boolean ->
                    setState {
                        state.teamMemberInputs[i].inputValue = value
                        state.teamMemberInputs[i].isRed = isRed
                    }
                }
            }
        }
    }
}