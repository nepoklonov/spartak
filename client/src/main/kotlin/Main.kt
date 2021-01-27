import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import model.TeamMemberDTO
import react.buildElements
import react.dom.render
import services.TeamService
import styled.styledDiv
import view.ApplicationComponent
import kotlinx.browser.document
import react.router.dom.RouteComponent
import react.router.dom.browserRouter
import routes.Routes
import services.AdminService
import view.AuthFormComponent

import kotlin.coroutines.CoroutineContext

private class Application : CoroutineScope {
    override val coroutineContext: CoroutineContext = Job()

    fun start() {
        val teamMembersService = TeamService(coroutineContext)
         launch {
            teamMembersService.addTeamMember(
                    TeamMemberDTO(-1, 1, "Савелий", "Жопа", "вратарь", "02.02.2000", "Москоу")
            ).also { console.log(it) }

            teamMembersService.getTeamMemberById(1).also { console.log(it) }

            teamMembersService.editTeamMember(
                    Json.encodeToString(TeamMemberDTO.serializer(), TeamMemberDTO(1, 1, "Савелий", "Светлый", "враторь", "02.02.2000", "Москоу"))
            ).also { console.log(it) }

            teamMembersService.getTeamMemberById(1).also { console.log(it) }


            teamMembersService.deleteTeamMemberById(1).also { console.log(it) }

            teamMembersService.getTeamMemberById(1).also { console.log(it) }
         }

        document.getElementById("react-app")?.let {
            render(buildElements {
                child(Routes::class) {
                    child(ApplicationComponent::class) {
                        attrs.coroutineScope = this@Application
                    }
                    child(AuthFormComponent::class) {
                    }
                }

            }, it)
        }
    }
}

fun main() {
    GlobalStyles.inject()
    Application().start()
}