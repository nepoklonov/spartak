package services

import rpc.RPCService
import java.io.File

actual class HtmlService: RPCService {
    actual suspend fun editHtml(url: String, content: String): Boolean {
        File(url).writeText(content)
        return true
    }
}