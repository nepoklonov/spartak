package pages.Games

import Consts.gameInputs
import Consts.tableHeaders
import adminPageComponents.AdminButtonType
import adminPageComponents.FormComponent
import adminPageComponents.Input
import adminPageComponents.adminButton
import content
import grid
import gridArea
import gridTemplateAreas
import header
import isAdmin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.js.onSubmitFunction
import model.GameDTO
import model.TeamDTO
import navigation
import react.*
import services.GameService
import services.TeamService
import styled.css
import styled.styledDiv
import styled.styledForm
import tableContent
import tableHeader

data class GameWithTeams(
    val id: Int,
    val date: String,
    val time: String?,
    val teamA: TeamDTO? = null,
    val teamB: TeamDTO? = null,
    val stadium: String,
    val result: String?,
)

external interface GamesProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedChampionship: String
}

class GamesState : RState {
    var allGamesWithTeams: List<GameWithTeams>? = null
    var inputs: MutableMap<String, Input> = gameInputs
    var addGameForm: Boolean = false
    var editGameForm: GameWithTeams? = null
}

class Games : RComponent<GamesProps, GamesState>() {
    init {
        state = GamesState()
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    private fun getState(year: String, coroutineScope: CoroutineScope) {
        val gameService = GameService(coroutineContext)
        val teamsService = TeamService(coroutineContext)

        coroutineScope.launch {

            val allGames = gameService.getAllGamesByYear(year)

            val allGamesWithTeams: MutableList<GameWithTeams> = mutableListOf()

            allGames.forEach {
                val teamA = teamsService.getTeamById(it.teamAId)
                val teamB = teamsService.getTeamById(it.teamBId)

                allGamesWithTeams += GameWithTeams(it.id!!, it.date, it.time, teamA, teamB, it.stadium, it.result)
            }

            val teamList = teamsService.getNavigationList()

            var teamMap = mapOf<String, String>()

            teamList.forEach {
                teamMap += it.id.toString() to it.header
            }


            setState {
                this.allGamesWithTeams = allGamesWithTeams
                this.inputs["teamA"]!!.options = teamMap
                this.inputs["teamB"]!!.options = teamMap
            }
        }
    }

    override fun componentDidMount() {
        getState(props.selectedChampionship, props.coroutineScope)
    }

    override fun componentDidUpdate(prevProps: GamesProps, prevState: GamesState, snapshot: Any) {
        if (this.props.selectedChampionship != prevProps.selectedChampionship) {
            getState(props.selectedChampionship, props.coroutineScope)
        }
    }


    override fun RBuilder.render() {
        grid {

            header {
                +"Расписание игр"
            }

            navigation {
                child(GamesNavigation::class){
                   attrs.coroutineScope = props.coroutineScope
                }
            }

            content {
                styledDiv {
                    css {
                        display = Display.grid
                        gridTemplateAreas(
                            "date time teamA teamB stadium result"
                        )
                    }

                    tableHeaders.keys.forEach { key ->
                        tableHeader {
                            css {
                                gridArea = key
                                fontFamily = "Russo"
                                fontSize = 20.px
                            }
                            +(tableHeaders[key] ?: error(""))
                        }
                    }
                    //TODO: с этим тоже что-нибудь сделать бы..
                    // JS точно умеет превращать объект в JSON,
                    // а вам, по сути, нужен список полей
                    // думаю, можно что-нибудь клёвое сделать
                    state.allGamesWithTeams?.forEach { game ->
                        tableContent {
                            +game.date
                        }
                        tableContent {
                            +game.time!!
                        }
                        tableContent {
                            +game.teamA?.name!!
                        }
                        tableContent {
                            +game.teamB?.name!!
                        }
                        tableContent {
                            +game.stadium
                        }
                        tableContent {
                            +game.result!!
                        }


                        if (isAdmin) {
                            adminButton(AdminButtonType.Delete){
                                val gameService = GameService(coroutineContext)
                                props.coroutineScope.launch {
                                    gameService.deleteGame(game.id)
                                }
                            }
                            if (state.editGameForm != game) {
                                adminButton(AdminButtonType.Edit){
                                    setState {
                                        editGameForm = game
                                        inputs["date"]!!.inputValue = game.date
                                        inputs["time"]!!.inputValue = game.time ?: ""
                                        inputs["teamA"]!!.inputValue = game.teamA?.link ?: ""
                                        inputs["teamB"]!!.inputValue = game.teamB?.link ?: ""
                                        inputs["stadium"]!!.inputValue = game.stadium
                                        inputs["result"]!!.inputValue = game.result ?: ""
                                    }
                                }
                            } else {
                                styledDiv {
                                    styledForm {
                                        attrs.onSubmitFunction = { event ->
                                            event.preventDefault()
                                            event.stopPropagation()
                                            val gameService = GameService(coroutineContext)
                                            props.coroutineScope.launch {
                                                var formIsCompleted = true
                                                state.inputs.values.forEach {
                                                    if (it.isRed) {
                                                        formIsCompleted = false
                                                    }
                                                }
                                                if (formIsCompleted) {
                                                    gameService.editGame(
                                                        GameDTO(
                                                            game.id,
                                                            state.inputs["date"]!!.inputValue,
                                                            state.inputs["time"]!!.inputValue,
                                                            props.selectedChampionship,
                                                            state.inputs["teamA"]!!.inputValue.toInt(),
                                                            state.inputs["teamB"]!!.inputValue.toInt(),
                                                            state.inputs["stadium"]!!.inputValue,
                                                            state.inputs["result"]!!.inputValue,
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                        child(FormComponent::class) {
                                            console.log(state.inputs)
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

                if (isAdmin) {
                    if (!state.addGameForm) {
                        adminButton(AdminButtonType.Add){
                            setState {
                                addGameForm = true
                            }
                        }
                    } else {
                        styledForm {
                            attrs.onSubmitFunction = { event ->
                                event.preventDefault()
                                event.stopPropagation()
                                val gameService = GameService(coroutineContext)
                                props.coroutineScope.launch {
                                    var formIsCompleted = true
                                    state.inputs.values.forEach {
                                        if (it.isRed) {
                                            formIsCompleted = false
                                        }
                                    }
                                    if (formIsCompleted) {
                                        gameService.addGame(
                                            GameDTO(
                                                null,
                                                state.inputs["date"]!!.inputValue,
                                                state.inputs["time"]!!.inputValue,
                                                props.selectedChampionship,
                                                state.inputs["teamA"]!!.inputValue.toInt(),
                                                state.inputs["teamB"]!!.inputValue.toInt(),
                                                state.inputs["stadium"]!!.inputValue,
                                                state.inputs["result"]!!.inputValue,
                                            )
                                        )
                                    }
                                }
                            }
                            child(FormComponent::class) {
                                attrs.inputs = state.inputs
                                attrs.updateState = { key: String, value: String, isRed: Boolean ->
                                    setState {
                                        state.inputs[key]!!.inputValue = value
                                        state.inputs[key]!!.isRed = isRed
                                    }
                                }
                                attrs.addOtherOption = { isItTeamA: Boolean, teamName: String ->
                                    val teamService = TeamService(coroutineContext)
                                    props.coroutineScope.launch {
                                        val team = TeamDTO(
                                            null,
                                            teamName,
                                            null,
                                            false,
                                            null
                                        )
                                        val id = teamService.addTeam(team)
                                        team.id = id
                                        setState {
                                            if (isItTeamA) {
                                                inputs["teamA"]!!.inputValue = id.toString()
                                            } else {
                                                inputs["teamB"]!!.inputValue = id.toString()
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
