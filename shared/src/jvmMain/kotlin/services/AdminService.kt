package services

import database.Admins
import database.database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

actual class AdminService {
    actual suspend fun checkAdmin(login: String, password: String): Boolean {
        return database {
            Admins.select{
                (Admins.login eq login) and (Admins.password eq password)
            }.empty()
        }
    }
}
