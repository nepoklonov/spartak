package database

import org.jetbrains.exposed.dao.id.IntIdTable

object Teams : IntIdTable() {
    val name = text("name")
    val link = text("link")
    val isOur = bool("isOur")
    val year = text("year")
}