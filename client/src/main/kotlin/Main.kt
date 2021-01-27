
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.id
import model.TeamMemberDTO
import pages.*
import react.buildElements
import react.dom.div
import react.dom.render
import react.router.dom.browserRouter
import react.router.dom.route
import react.router.dom.switch
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


            teamMembersService.deleteTeamMemberById(1).also { console.log(it) }

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
                            div {
                                switch {
//                                    redirect(from = "/", to = "/main")
                                    route("/main", Main::class, exact = true)
                                    route("/admin", Admin::class, exact = true)
                                    route("/page", SomePage::class, exact = true)

                                    route("/news", News::class, exact = true)
                                    route("/games/championship2003", Games::class, exact = true)



                                    route("/club", Club::class, exact = true)
                                    route("/recruitment", Recruitment::class, exact = true)
                                    route("/workouts", Workouts::class, exact = true)
                                    route("/summerCamp", SummerCamp::class, exact = true)
                                    route("/gallery", Gallery::class, exact = true)
                                }
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