@file:Suppress("BlockingMethodInNonBlockingContext")

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
import kotlinx.coroutines.*
import kotlinx.css.*
import kotlinx.html.*
import rpc.rpc
import services.*
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.*


fun Application.main() {
    install(ContentNegotiation) {
        jackson {}
    }
    install(Sessions) {
        cookie<LoginSession>("login-session", SessionStorageMemory()) {
            cookie.path = "/"
        }
    }

    createDatabases()

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
            rpc(HtmlService::class)

            post("upload/image") {
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

                val multipart = call.receiveMultipart()
                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            val uploadDir = "uploads"  // CREATED LINE
                            val ext = File(part.originalFileName!!).extension
                            val file = File(uploadDir, "upload-${System.currentTimeMillis()}.$ext")
                            part.streamProvider().use { input ->
                                file.outputStream().buffered().use { output -> input.copyToSuspend(output) }
                            }
                        }
                        else -> TODO()
                    }

                    part.dispose()
                }
            }
        }
    }
}