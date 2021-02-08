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

val roleList = listOf("Защитники", "Вратари", "Нападающие")


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
                    state.navigationList!!.forEach { teamsNavigationProps ->
                        route<SmallNavigationProps>("/teams/:selectedLink") { linkProps ->
                            child(SmallNavigation::class) {
                                attrs.string = teamsNavigationProps.header
                                attrs.link = teamsNavigationProps.link
                                attrs.selectedLink = linkProps.match.params.selectedLink
                            }
                        }
                    }
                    child(SmallNavigationForm::class) {
                        attrs.isTeam = true
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

                }else {
                    +"Загрузка..."
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
            }else {
                +"Загрузка..."
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
                attrs.updateState = { i: Int, value: String, isRed: Boolean ->
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
                attrs.updateState = { i: Int, value: String, isRed: Boolean ->
                    setState {
                        state.teamMemberInputs[i].inputValue = value
                        state.teamMemberInputs[i].isRed = isRed
                    }
                }
            }
        }
    }
}