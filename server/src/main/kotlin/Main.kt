import database.Checks
import database.database
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.html.respondHtml
import io.ktor.http.content.files
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.jackson.jackson
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import kotlinx.coroutines.launch
import kotlinx.html.*
import model.Check
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import rpc.rpc
import services.CheckService

fun Application.main() {
    install(ContentNegotiation) {
        jackson {}
    }

    database {
        SchemaUtils.create(Checks)
    }

    launch {

        database {
            Checks.insert {
                it[checkId] = 1
                it[checkText] = "Everything is fine. Thanks."
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
        get("/admin") {
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
        }
    }
}