package view

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import model.Check
import model.NewsDTO
import model.TeamMemberDTO
import react.*
import react.router.dom.navLink
import services.CheckService
import services.NewsService
import services.TeamService
import styled.styledDiv

external interface ApplicationProps : RProps {
    var coroutineScope: CoroutineScope
}

class ApplicationState : RState {
    var error: Throwable? = null
    var check: Check? = null
    var teamMember: TeamMemberDTO? = null
}

class ApplicationComponent : RComponent<ApplicationProps, ApplicationState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val checkService = CheckService(coroutineContext)
        val newsService = NewsService(coroutineContext)

        props.coroutineScope.launch {
//            newsService.addNews(NewsDTO("news/1.html"))
//            newsService.addNews(NewsDTO("news/2.html"))
//            newsService.addNews(NewsDTO("news/3.html"))
            val check = try {
                checkService.getCheck()
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }

            val teamMember = try {
//                teamService.getTeamMemberByTeamIdAndRole("НП",)
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }

            setState {
                this.check = check
//                this.teamMember = teamMember
            }
        }
    }

    override fun RBuilder.render() {
        val error = state.error
        if (error != null) {
            throw error
        }
        styledDiv {
            +(state.check?.checkText ?: "Let's wait.")
            +(state.teamMember?.firstName ?: "Let's wait too...")
        }

        navLink<ApplicationProps>("/page"){
            +"page"
        }
    }
}