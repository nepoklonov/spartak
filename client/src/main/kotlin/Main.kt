import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import model.TeamMemberDTO
import react.buildElements
import react.dom.render
import services.TeamMembersService
import styled.styledDiv
import view.ApplicationComponent
import kotlin.coroutines.CoroutineContext

private class Application : CoroutineScope {
    override val coroutineContext: CoroutineContext = Job()

    fun start() {

        val teamMembersService = TeamMembersService(coroutineContext)
        launch {
            teamMembersService.addTeamMember(
                    Json.encodeToString(TeamMemberDTO.serializer(), TeamMemberDTO(null, 1, "Савелий", "Жопа", "вратарь", "02.02.2000", "Москоу"))
            ).also { console.log(it) }

            teamMembersService.getTeamMemberById("1").also { console.log(it) }

            teamMembersService.editTeamMember(
                    Json.encodeToString(TeamMemberDTO.serializer(), TeamMemberDTO(null, 1, "Савелий", "Светлый", "враторь", "02.02.2000", "Москоу"))
            ).also { console.log(it) }

            teamMembersService.getTeamMemberById("1").also { console.log(it) }


            teamMembersService.deleteTeamMemberById("1").also { console.log(it) }

            teamMembersService.getTeamMemberById("1").also { console.log(it) }
        }

        document.getElementById("react-app")?.let {
            render(buildElements {
                styledDiv { +"Let's check!" }
                child(ApplicationComponent::class) {
                    attrs.coroutineScope = this@Application
                }
            }, it)
        }
    }
}

fun main() {
    GlobalStyles.inject()
    Application().start()
}