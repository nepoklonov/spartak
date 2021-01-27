package view

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import model.Check
import model.TeamMemberDTO
import pages.Admin
import pages.Main
import pages.SomePage
import react.*
import react.dom.div
import react.dom.nav
import react.router.dom.browserRouter
import react.router.dom.navLink
import react.router.dom.route
import react.router.dom.switch
import services.CheckService
import services.TeamService
import styled.styledDiv
import react.router.dom.navLink

external interface ApplicationProps : RProps {
    var coroutineScope: CoroutineScope
}

class ApplicationState : RState {
    var error: Throwable? = null
    var check: Check? = null
    var teamMember: TeamMemberDTO? = null
}

class ApplicationComponent : RComponent<ApplicationProps, ApplicationState>() {
    init {
        state = ApplicationState()
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val checkService = CheckService(coroutineContext)
        val teamService = TeamService(coroutineContext)

        props.coroutineScope.launch {
            val check = try {
                checkService.getCheck()
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }

            val teamMember = try {
                teamService.getTeamMemberById(1)
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }

            setState {
                this.check = check
                this.teamMember = teamMember
            }
        }
    }

    override fun RBuilder.render() {
        val error = state.error
        if (error != null) {
            throw error
        }
        styledDiv {

        }
    }
}