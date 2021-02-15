import database.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.css.*
import kotlinx.css.Float
import kotlinx.css.properties.LineHeight
import kotlinx.css.properties.TextDecoration
import kotlinx.css.properties.lh
import kotlinx.html.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SchemaUtils
import rpc.rpc
import services.*
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.*


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
        fontSize = 42.667.px
        margin = 50.px.toString()
        fontWeight = FontWeight.normal
    }
    h2 {
        fontFamily = "Russo"
        fontWeight = FontWeight.normal
    }
    h3 {
        fontFamily = "Russo"
        fontWeight = FontWeight.normal
    }
    "*" {
        fontFamily = "PT"
        fontSize = 20.px
        lineHeight = LineHeight.normal
    }
    button {
        backgroundColor = Color("#9D0707")
        hover {
            backgroundColor = Color("#660c0c")
        }
        border = "none"
        textDecoration = TextDecoration.none
        fontFamily = "Russo"
        color = Color.white
        fontSize = 14.pt
        padding = 15.px.toString()
        paddingLeft = 50.px
        paddingRight = 50.px
        cursor = Cursor.pointer
        margin = 10.px.toString()
    }
    rule(".news-img") {
        width = 32.pct
        padding = 1.pct.toString()
        height = LinearDimension.auto
        float = Float.left
    }
    rule(".news-div") {
        padding = 1.pct.toString()
        minWidth = 30.pct
    }
    rule(".summer-camp-img, .main-img") {
        width = 40.pct
        padding = 50.px.toString()
        height = LinearDimension.auto
    }
    rule(".summer-camp-div, .main") {
        display = Display.flex
        justifyContent = JustifyContent.spaceAround
    }
    rule(".summer-camp-content, .main-content") {
        padding = 50.px.toString()
        alignContent = Align.center
        display = Display.block
    }
}


fun Application.main() {
    install(ContentNegotiation) {
        jackson {}
    }
    install(Sessions) {
        cookie<LoginSession>("login-session", SessionStorageMemory()) {
            cookie.path = "/"
        }
    }

    database {
        SchemaUtils.create(Checks)
        SchemaUtils.create(TeamMembers)
        SchemaUtils.create(Games)
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

    routing {
        get("{...}") {
            val role = call.sessions.get<LoginSession>()?.role ?: Role.Basic
            if (role == Role.Admin) {
                call.response.cookies.append("role", "admin")
            }
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
            rpc(MainNavigationService::class)

            post("upload/image") {
                var location: String? = null
                val files = mutableListOf<File>()
                val uploadDir = "uploads"
                val timestamp = createTimestamp("yyyy-MM-dd-HH-mm-ss-SSS")
                var counter = 0
                val multipart = call.receiveMultipart()

                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            if (part.name == "location") {
                                location = part.value
                            }
                        }

                        is PartData.FileItem -> {
                            val suffix = randomString(8L)
                            val fileName = "$timestamp-$counter-$suffix"
                            val extension = part.originalFileName?.let { File(it) }?.extension ?: "file"
                            val file = File(uploadDir, "$fileName.$extension").also { it.parentFile.mkdirs() }
                            files += file
                            part.streamProvider().use { input ->
                                file.outputStream().buffered().use { output -> input.copyToSuspend(output) }
                            }
                        }
                        is PartData.BinaryItem -> TODO()
                    }
                    part.dispose()
                    counter += 1
                }

                location
                    ?.takeIf { it.startsWith("uploads") || it.startsWith("images") }
                    ?.takeIf { !it.contains("..") } //perfect task for the interview
                    ?.let { dir ->
                        files.replaceAll { file ->
                            File(dir, file.name)
                                .also { it.parentFile.mkdirs() }
                                .takeIf { !it.exists() }
                                ?.also { destination ->
                                    file.copyTo(destination)
                                    file.delete()
                                } ?: file
                        }
                    }
                val address: File.() -> String = { "$parent/$name" }
                when (files.size) {
                    0 -> call.respond(HttpStatusCode.BadRequest)
                    1 -> call.respondText(files[0].address())
                    else -> call.respondText {
                        Json.encodeToString(ListSerializer(String.serializer()), files.map(address))
                    }
                }
            }
        }
    }
}


@Suppress("BlockingMethodInNonBlockingContext")
suspend fun InputStream.copyToSuspend(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    yieldSize: Int = 4 * 1024 * 1024,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Long {
    return withContext(dispatcher) {
        val buffer = ByteArray(bufferSize)
        var bytesCopied = 0L
        var bytesAfterYield = 0L
        while (true) {
            val bytes = read(buffer).takeIf { it >= 0 } ?: break
            out.write(buffer, 0, bytes)
            if (bytesAfterYield >= yieldSize) {
                yield()
                bytesAfterYield %= yieldSize
            }
            bytesCopied += bytes
            bytesAfterYield += bytes
        }
        return@withContext bytesCopied
    }
}