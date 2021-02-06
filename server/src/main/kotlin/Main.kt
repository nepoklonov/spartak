import database.*
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
import services.*


private val globalCss = CSSBuilder().apply {
    fontFace {
        fontFamily = "Russo"
        put("src", "url('/fonts/Russo-One.ttf')")
    }
    fontFace {
        fontFamily = "PT"
        put("src", "url('/fonts/PT-Sans.ttf')")
    }

    body {
        margin(0.px)
        padding(0.px)

        fontSize = 13.px
        fontFamily =
            "-system-ui, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Oxygen, Ubuntu, Cantarell, Droid Sans, Helvetica Neue, BlinkMacSystemFont, Segoe UI, Roboto, Oxygen, Ubuntu, Cantarell, Droid Sans, Helvetica Neue, Arial, sans-serif"

        lineHeight = 20.px.lh
    }
    h1 {
        fontFamily = "Russo"
    }
    h2 {
        fontFamily = "Russo"
    }
    h3 {
        fontFamily = "Russo"
    }
}


fun Application.main() {
    install(ContentNegotiation) {
        jackson {}
    }

    database {
        SchemaUtils.create(Checks)
        SchemaUtils.create(TeamMembers)
        SchemaUtils.create(GameCalendar)
        SchemaUtils.create(Trainers)
        SchemaUtils.create(Photos)
        SchemaUtils.create(Teams)
        SchemaUtils.create(Timetable)
        SchemaUtils.create(Admins)
        SchemaUtils.create(Recruitment)
        SchemaUtils.create(News)
    }

    launch {

        database {
            Admins.insert {
                it[login] = "admin"
                it[password] = "admin"
            }
        }

        database {
            Checks.insert {
                it[checkId] = 1
                it[checkText] = "Everything is fine. Thanks."
            }
        }
        for (i in 1 until 20) {
            database {
                Photos.insert {
                    it[url] = "logo.png"
                    it[gallerySection] = "trainingProcess"
                }
            }
        }
        for (i in 1 until 4) {
            database {
                News.insert {
                    it[url] = "news/$i.html"
                }
            }
        }
        for (i in 1 until 20) {
            database {
                Photos.insert {
                    it[url] = "vk.png"
                    it[gallerySection] = "LadogaCup2019"
                }
            }
        }
        database {
            TeamMembers.insert {
                it[teamId] = 1
                it[photo] = "logo.png"
                it[firstName] = "Змейка"
                it[lastName] = "Гитарова"
                it[role] = "НП"
                it[birthday] = "28.08.2019"
                it[city] = "г.Ейск"
            }
        }

        database {
            GameCalendar.insert {
                it[date] = "dhzkjfh"
                it[time] = "Змейка"
                it[year] = "championship2003"
                it[teamAId] = 1
                it[teamBId] = 2
                it[stadium] = "28.08.2019"
                it[result] = "г.Ейск"
            }
        }
        database {
            Trainers.insert {
                it[teamId] = 1
                it[name] = "2003"
                it[photo] = "logo.png"
                it[dateOfBirth] = "2003"
                it[info] = "drfghygtfrdesdrftgyhujhygtfrdesrftgyhujiuhygtrfeddrftgyhukjiuhygtrfedswredrftgyhu"
            }
        }
        database {
            Teams.insert {
                it[name] = "spartak"
                it[isOur] = true
                it[type] = ""
                it[year] = "2003"
                it[trainerId] = 1
            }
        }
        database {
            Teams.insert {
                it[name] = "ne spartak"
                it[isOur] = false
                it[type] = ""
                it[year] = "2003"
                it[trainerId] = 1
            }
        }
        database {
            Timetable.insert {
                it[datetime] = 1612020600000.0
                it[teamId] = 1
                it[type] = "shhm"
                it[place] = "ура наконец-то я доделала это ебанное расписание"
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
        static("htmlPages") { files("htmlPages") }
        static("news") { files("news") }


        route("/api") {
            rpc(CheckService::class)
            rpc(GameService::class)
            rpc(TeamService::class)
            rpc(TimetableService::class)
            rpc(TrainerService::class)
            rpc(PhotoService::class)
            rpc(AdminService::class)
            rpc(RecruitmentService::class)
            rpc(NewsService::class)
        }
    }
}