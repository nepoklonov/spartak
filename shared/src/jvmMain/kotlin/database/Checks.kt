package database

import org.jetbrains.exposed.dao.id.IntIdTable

object Checks : IntIdTable() {
    val checkId = integer("checkId")
    val checkText = text("checkText")
}