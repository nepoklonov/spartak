package pages.Teams

import Consts.roleMap
import adminPageComponents.AdminButtonType
import adminPageComponents.FormComponent
import adminPageComponents.Input
import adminPageComponents.adminButton
import content
import grid
import header
import isAdmin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.js.onSubmitFunction
import model.TeamDTO
import model.TeamMemberDTO
import model.TrainerDTO
import navigation
import pageComponents.RedFrameComponent
import react.*
import services.TeamService
import services.TrainerService
import smallHeaderText
import styled.css
import styled.styledDiv
import styled.styledForm

external interface TeamsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedTeam: String
}

class TeamsState : RState {
    var team: TeamDTO? = null
    var trainer: TrainerDTO? = null
    var teamMembersWithRoles: Map<String, List<TeamMemberDTO>>? = null
    var teamInputs: MutableMap<String, Input> = Consts.teamInputs
    var trainerInputs: MutableMap<String, Input> = Consts.trainerInputs
    var teamMemberInputs: MutableMap<String, Input> = Consts.teamMemberInputs
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
            val team = teamService.getTeamByLink(selectedTeam)

            if (team.id != null) {
                val trainer = if (team.link != null) {
                    trainerService.getTrainerByLink(team.link!!)
                } else {
                    null
                }
                val teamMembersWithRoles = mutableMapOf<String, List<TeamMemberDTO>>()

                roleMap.forEach { role ->
                    val teamMembers = teamService.getTeamMemberByRoleAndTeam(role.key, team.link!!)
                    teamMembersWithRoles[role.value] = teamMembers
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
        grid {
            navigation {
                child(TeamsNavigation::class){
                    attrs.coroutineScope = props.coroutineScope
                    attrs.selectedTeam = props.selectedTeam
                }
            }

            header {
                +"Команды"
            }

            content {
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
                            if (isAdmin) {
                                adminButton(AdminButtonType.Delete) {
                                    val trainerService = TrainerService(coroutineContext)
                                    props.coroutineScope.launch {
                                        trainerService.deleteTrainer(trainer!!.id!!)
                                    }
                                }
                                if (state.editTrainerForm != trainer) {
                                    adminButton(AdminButtonType.Edit) {
                                        setState {
                                            editTrainerForm = trainer
                                            state.trainerInputs["name"]!!.inputValue = trainer!!.name
                                            state.trainerInputs["info"]!!.inputValue = trainer.info
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
                                        child(FormComponent::class) {
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
                    state.teamMembersWithRoles!!.forEach { teamMembers ->
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
                                if (isAdmin) {
                                    adminButton(AdminButtonType.Delete) {
                                        val teamService = TeamService(coroutineContext)
                                        props.coroutineScope.launch {
                                            teamService.deleteTeamMember(teamMember.id!!)
                                        }
                                    }

                                    if (state.editTeamMemberForm != teamMember) {
                                        adminButton(AdminButtonType.Edit) {
                                            setState {
                                                editTeamMemberForm = teamMember
                                                teamMemberInputs["number"]!!.inputValue = teamMember.number
                                                teamMemberInputs["firstName"]!!.inputValue = teamMember.firstName
                                                teamMemberInputs["lastName"]!!.inputValue = teamMember.lastName
                                                teamMemberInputs["role"]!!.inputValue = teamMember.role
                                                teamMemberInputs["birthday"]!!.inputValue = teamMember.birthday
                                                teamMemberInputs["city"]!!.inputValue = teamMember.city
                                                teamMemberInputs["teamRole"]!!.inputValue = teamMember.number
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
                                            child(FormComponent::class) {
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
                if (isAdmin) {
                    if (!state.addTeamMemberForm) {
                        adminButton(AdminButtonType.Add) {
                            setState {
                                addTeamMemberForm = true
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
                            child(FormComponent::class) {
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