package services

import database.Checks
import database.database
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import model.Check
import org.jetbrains.exposed.sql.selectAll
import rpc.RPCService

actual class CheckService : RPCService {
    actual suspend fun getCheck(): Check {
        delay(2000)
        return database {
            Checks.selectAll().first().let {
                Check(it[Checks.checkId], it[Checks.checkText])
            }
        }
    }
}