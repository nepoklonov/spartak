import database.Checks
import database.TeamMembers
import database.database
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.routing.*
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.lh
import kotlinx.html.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import rpc.rpc
import services.CheckService
import services.TeamService


private val globalCss = CSSBuilder().apply {
    body {
        margin(0.px)
        padding(0.px)

        fontSize = 13.px
        fontFamily = "-system-ui, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Oxygen, Ubuntu, Cantarell, Droid Sans, Helvetica Neue, BlinkMacSystemFont, Segoe UI, Roboto, Oxygen, Ubuntu, Cantarell, Droid Sans, Helvetica Neue, Arial, sans-serif"

        lineHeight = 20.px.lh
    }
}


fun Application.main() {
    install(ContentNegotiation) {
        jackson {}
    }

    database {
        SchemaUtils.create(Checks)
        SchemaUtils.create(TeamMembers)
    }

    launch {

        database {
            Checks.insert {
                it[checkId] = 1
                it[checkText] = "Everything is fine. Thanks."
            }
        }

        database {
            TeamMembers.insert {
                it[teamId] = 1
                it[firstName] = "Змейка"
                it[lastName] = "Гитарова"
                it[role] = "начальная подготовка -- НП"
                it[birthday] = "28.08.2019"
                it[city] = "г.Ейск"
            }
        }
    }

    routing {
        get("{...}") {
            call.respondHtml {
                head {
                    meta {
                        charset = "utf-8"
                    }
                    title {
                        +"Kotlin full stack application demo"
                    }
                    style {
                        unsafe {
                            +globalCss.toString()
                        }
                    }
                }
                body {
                    div {
                        id = "react-app"
                        +"Loading..."
                    }
                    script(src = "/resources/client.js") { }
                }
            }
        }

        static("resources") {
            resources("/")
        }

        static("images") { files("images") }
        static("fonts") { files("fonts") }
        static("fonts") { files("htmlPages") }


        route("/api") {
            rpc(CheckService::class)
            rpc(TeamService::class)
        }
    }
}