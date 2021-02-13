import database.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.sessions.*
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
    install(Sessions) {
        cookie<LoginSession> ("login-session", SessionStorageMemory()) {
            cookie.path = "/"
        }
    }

    database {
        SchemaUtils.create(Checks)
        SchemaUtils.create(TeamMembers)
        SchemaUtils.create(GameCalendar)
        SchemaUtils.create(Trainers)
        SchemaUtils.create(Photos)
        SchemaUtils.create(Teams)
        SchemaUtils.create(Workouts)
        SchemaUtils.create(Admins)
        SchemaUtils.create(Recruitment)
        SchemaUtils.create(News)
        SchemaUtils.create(GallerySections)
        SchemaUtils.create(GamesSections)
        SchemaUtils.create(WorkoutsSections)
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
                    it[url] = "/newsHtml/$i.html"
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
                it[teamLink] = "2003"
                it[photo] = "logo.png"
                it[firstName] = "Змейка"
                it[lastName] = "Гитарова"
                it[role] = "defenders"
                it[birthday] = "28.08.2019"
                it[city] = "г.Ейск"
                it[teamRole] = "к"
                it[number] = "1"
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
                it[teamLink] = "2003"
                it[name] = "2003"
                it[photo] = "logo.png"
                it[info] = "drfghygtfrdesdrftgyhujhygtfrdesrftgyhujiuhygtrfeddrftgyhukjiuhygtrfedswredrftgyhu"
            }
        }
        database {
            Teams.insert {
                it[name] = "spartak"
                it[link] = "2003"
                it[isOur] = true
                it[year] = "2003"
            }
        }
        database {
            Teams.insert {
                it[name] = "ne spartak"
                it[link] = "2004"
                it[isOur] = false
                it[year] = "2003"
            }
        }
        database {
            Workouts.insert {
                it[datetime] = 1612020600000.0
                it[sectionLink] = "shhm"
                it[text] = "ура наконец-то я доделала это ебанное расписание"
            }
        }
        database {
            GallerySections.insert {
                it[header] = "Тренировочный процесс"
                it[link] = "trainingProcess"
            }
        }
        database {
            GallerySections.insert {
                it[header] = "Кубок Ладоги 2019"
                it[link] = "LadogaCup2019"
            }
        }
        database {
            GallerySections.insert {
                it[header] = "Пекин"
                it[link] = "Beijing"
            }
        }
        database {
            GallerySections.insert {
                it[header] = "Турнир 2011 г.р."
                it[link] = "championship2011"
            }
        }
        database {
            GamesSections.insert {
                it[header] = "Первенство СПБ 2003"
                it[link] = "championship2003"
            }
        }
        database {
            GamesSections.insert {
                it[header] = "Первенство СПБ 2004"
                it[link] = "championship2004"
            }
        }
        database {
            GamesSections.insert {
                it[header] = "Первенство СПБ 2006"
                it[link] = "championship2006"
            }
        }
        database {
            WorkoutsSections.insert {
                it[header] = "ШХМ"
                it[link] = "shhm"
            }
        }
        database {
            WorkoutsSections.insert {
                it[header] = "Спартак 2013"
                it[link] = "2013"
            }
        }
        database {
            WorkoutsSections.insert {
                it[header] = "Спартак 2003-2004"
                it[link] = "2003"
            }
        }
        database {
            WorkoutsSections.insert {
                it[header] = "Спартак 2005"
                it[link] = "2005"
            }
        }
        database {
            WorkoutsSections.insert {
                it[header] = "Вратарские Тренировки"
                it[link] = "goalkeepers"
            }
        }
        database {
            WorkoutsSections.insert {
                it[header] = "Группа набора"
                it[link] = "recruitment"
            }
        }
        database {
            WorkoutsSections.insert {
                it[header] = "Спартак 2006"
                it[link] = "2006"
            }
        }
        database {
            WorkoutsSections.insert {
                it[header] = "Спартак Красная Ракета"
                it[link] = "red"
            }
        }
        database {
            WorkoutsSections.insert {
                it[header] = "Спартак 2008"
                it[link] = "2008"
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
        static("newsHtml") { files("newsHtml") }


        route("/api") {
            rpc(CheckService::class)
            rpc(GameService::class)
            rpc(TeamService::class)
            rpc(WorkoutsService::class)
            rpc(TrainerService::class)
            rpc(PhotoService::class)
            rpc(AdminService::class, AdminService::checkAdmin to { call, result ->
                if (result as Boolean) {
                    call.sessions.set(LoginSession(username = "admin", role = Role.Admin))
                }
            })
            rpc(RecruitmentService::class)
            rpc(NewsService::class)
            rpc(GalleryNavigationService::class)
            rpc(GamesNavigationService::class)
            rpc(WorkoutsNavigationService::class)
        }
    }
}