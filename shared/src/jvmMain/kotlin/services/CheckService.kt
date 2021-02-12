package services

import Annotations.RequireRole
import database.Checks
import database.database
import kotlinx.coroutines.delay
import model.Check
import org.jetbrains.exposed.sql.selectAll
import rpc.RPCService

actual class CheckService : RPCService {
    @RequireRole(Role.Admin)
    actual suspend fun getCheck(): Check {
        delay(200)
        return database {
            Checks.selectAll().first().let {
                Check(it[Checks.checkId], it[Checks.checkText])
            }
        }
    }
}