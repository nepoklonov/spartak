
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.id
import model.TeamMemberDTO
import react.buildElements
import react.dom.render
import react.router.dom.browserRouter
import services.TeamService
import styled.css
import styled.styledDiv
import view.FooterComponent
import view.HeaderComponent
import view.NavigationComponent
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
                TeamMemberDTO(1, 1, "Савелий", "Светлый", "враторь", "02.02.2000", "Москоу")
            ).also { console.log(it) }

            teamMembersService.getTeamMemberById(1).also { console.log(it) }


            teamMembersService.deleteTeamMember(1).also { console.log(it) }

            teamMembersService.getTeamMemberById(1).also { console.log(it) }
        }

        document.getElementById("react-app")?.let {
            render(buildElements {
                browserRouter {
                    styledDiv {
                        css {
                            backgroundImage = Image("url(/images/background.jpg)")
                            width = 100.pct
                        }
                        styledDiv {
                            attrs.id = "root"
                            css {
                                marginLeft = 100.px
                                marginRight = 100.px
                                backgroundColor = rgba(255, 255, 255, 0.5)
                            }
                            child(HeaderComponent::class) {}
                            child(NavigationComponent::class) {}
                            child(Router::class){
                                attrs.coroutineScope = this@Application
                            }
                            child(FooterComponent::class) {}
                        }
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