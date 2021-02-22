package pages

import content
import grid
import gridArea
import gridTemplateAreas
import header
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.js.onSubmitFunction
import model.GameDTO
import model.NavigationDTO
import model.TeamDTO
import navigation
import pageComponents.*
import react.*
import react.router.dom.route
import services.GameService
import services.GamesNavigationService
import services.TeamService
import styled.*
import tableContent
import tableHeader

val tableHeaders =
    mapOf(
        "date" to "Дата",
        "time" to "Время",
        "teamA" to "Команда А",
        "teamB" to "Команда Б",
        "stadium" to "Стадион",
        "result" to "Результат"
    )

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
    var error: Throwable? = null
    var gamesNavigationList: List<NavigationDTO>? = null
    var allGamesWithTeams: List<GameWithTeams>? = null
    var inputs: MutableMap<String, Input> = mutableMapOf(
        "date" to Input("Дата", "date", isNessesary = false),
        "time" to Input("Время", "time", isNessesary = false),
        "teamA" to Input(
            "Команда А",
            "teamAId",
            isSelect = true,
            options = mapOf(),
            allowOtherOption = true
        ),
        "teamB" to Input(
            "Команда Б",
            "teamBId",
            isSelect = true,
            options = mapOf(),
            allowOtherOption = true
        ),
        "stadium" to Input("Стадион", "stadium", isNessesary = false),
        "result" to Input("Результат", "result", isNessesary = false),
    )
    var smallNavigationForm: Boolean = false
    var editSmallNavigationForm: NavigationDTO? = null
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
        val gamesNavigationService = GamesNavigationService(coroutineContext)

        coroutineScope.launch {
            val gamesNavigationList = try {
                gamesNavigationService.getGamesNavigationList()
            } catch (e: Throwable) {
                console.log(e)
                setState {
                    error = e
                }
                return@launch
            }
            val allGames = try {
                gameService.getAllGamesByYear(year)
            } catch (e: Throwable) {
                console.log(e)
                setState {
                    error = e
                }
                return@launch
            }

            val allGamesWithTeams: MutableList<GameWithTeams> = mutableListOf<GameWithTeams>()

            allGames.forEach {
                val teamA = try {
                    teamsService.getTeamById(it.teamAId)
                } catch (e: Throwable) {
                    console.log(e)
                    setState {
                        error = e
                    }
                    return@launch
                }
                val teamB = try {
                    teamsService.getTeamById(it.teamBId)
                } catch (e: Throwable) {
                    console.log(e)
                    setState {
                        error = e
                    }
                    return@launch
                }

                allGamesWithTeams += GameWithTeams(it.id!!, it.date, it.time, teamA, teamB, it.stadium, it.result)
            }

            val teamList = try {
                teamsService.getNavigationList()
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }

            var teamMap = mapOf<String, String>()

            teamList.forEach {
                teamMap += it.id.toString() to it.header
            }


            setState() {
                this.allGamesWithTeams = allGamesWithTeams
                this.gamesNavigationList = gamesNavigationList
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
            navigation {
                if (state.gamesNavigationList != null) {
                    state.gamesNavigationList!!.forEach { gameNavigation ->
                        route<SmallNavigationProps>("/games/:selectedLink") { linkProps ->
                            child(SmallNavigation::class) {
                                attrs.string = gameNavigation.header
                                attrs.link = gameNavigation.link
                                attrs.selectedLink = linkProps.match.params.selectedLink
                            }
                        }
                        if (document.cookie.contains("role=admin")) {
                            child(AdminButtonComponent::class) {
                                attrs.updateState = {
                                    val gameNavigationService = GamesNavigationService(coroutineContext)
                                    props.coroutineScope.launch {
                                        gameNavigationService.deleteGamesSection(gameNavigation.id!!)
                                    }
                                }
                                attrs.type = "delete"
                            }
                            if (state.editSmallNavigationForm != gameNavigation) {
                                child(AdminButtonComponent::class) {
                                    attrs.updateState = {
                                        setState {
                                            editSmallNavigationForm = gameNavigation
                                        }
                                    }
                                    attrs.type = "edit"
                                }
                            } else {
                                child(SmallNavigationForm::class) {
                                    attrs.inputValues = listOf(gameNavigation.header, gameNavigation.link)
                                    attrs.addSection = { listOfInputValues ->
                                        val gamesNavigationService = GamesNavigationService(coroutineContext)
                                        props.coroutineScope.launch {
                                            gamesNavigationService.editGamesSection(
                                                NavigationDTO(
                                                    gameNavigation.id,
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
                                    val gamesNavigationService = GamesNavigationService(coroutineContext)
                                    props.coroutineScope.launch {
                                        gamesNavigationService.addGamesSection(
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
                +"Расписание игр"
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
                            +tableHeaders[key]!!
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


                        if (document.cookie.contains("role=admin")) {
                            child(AdminButtonComponent::class) {
                                attrs.updateState = {
                                    val gameService = GameService(coroutineContext)
                                    props.coroutineScope.launch {
                                        gameService.deleteGame(game.id)
                                    }
                                }
                                attrs.type = "delete"
                            }
                            if (state.editGameForm != game) {
                                child(AdminButtonComponent::class) {
                                    attrs.updateState = {
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
                                    attrs.type = "edit"
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
                                        child(FormViewComponent::class) {
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

                if (document.cookie.contains("role=admin")) {
                    if (!state.addGameForm) {
                        child(AdminButtonComponent::class) {
                            attrs.updateState = {
                                setState {
                                    addGameForm = true
                                }
                            }
                            attrs.type = "add"
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
                            child(FormViewComponent::class) {
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
