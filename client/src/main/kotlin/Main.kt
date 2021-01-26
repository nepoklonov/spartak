

import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import react.buildElements
import react.dom.render
import routes.Routes
import styled.styledDiv
import kotlin.coroutines.CoroutineContext

private class Application : CoroutineScope {
    override val coroutineContext: CoroutineContext = Job()

    fun start() {

//        val teamMembersService = TeamService(coroutineContext)
//        launch {
//            teamMembersService.addTeamMember(
//                    TeamMemberDTO(-1, 1, "Савелий", "Жопа", "вратарь", "02.02.2000", "Москоу")
//            ).also { console.log(it) }
//
//            teamMembersService.getTeamMemberById(1).also { console.log(it) }
//
//            teamMembersService.editTeamMember(
//                    Json.encodeToString(TeamMemberDTO.serializer(), TeamMemberDTO(1, 1, "Савелий", "Светлый", "враторь", "02.02.2000", "Москоу"))
//            ).also { console.log(it) }
//
//            teamMembersService.getTeamMemberById(1).also { console.log(it) }
//
//
//            teamMembersService.deleteTeamMemberById(1).also { console.log(it) }
//
//            teamMembersService.getTeamMemberById(1).also { console.log(it) }
//        }

        document.getElementById("react-app")?.let {
            render(buildElements {
                styledDiv { +"Let's check!" }
                child(Routes::class) {}
            }, it)
        }
    }
}

fun main() {
    GlobalStyles.inject()
    Application().start()
}