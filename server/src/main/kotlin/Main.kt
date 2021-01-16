
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
import kotlinx.html.*
import model.Check
import model.TeamMemberDTO
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import rpc.rpc
import services.CheckService
import services.TeamMembersService

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
        get("/") {
            call.respondHtml {
                head {
                    meta {
                        charset = "utf-8"
                    }
                    title {
                        +"Kotlin full stack application demo"
                    }
                }
                body {
                    div {
                        id = "react-app"
                        +"Loading..."
                    }
                    script(src = "/client.js") { }
                }
            }
        }

        static("/") {
            resources("/")
        }

        route("/api") {
            rpc(CheckService::class, Check.serializer())
            rpc(TeamMembersService::class, TeamMemberDTO.serializer())
        }
    }
}