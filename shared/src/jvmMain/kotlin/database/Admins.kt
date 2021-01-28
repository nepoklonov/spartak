package database

import org.jetbrains.exposed.dao.id.IntIdTable

object Admins : IntIdTable() {
    var login = text("login")
    val password = text("password")
}