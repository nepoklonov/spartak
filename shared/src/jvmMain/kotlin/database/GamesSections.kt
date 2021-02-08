package database

import org.jetbrains.exposed.dao.id.IntIdTable

object GamesSections: IntIdTable() {
    val header = text("name")
    val link = text("link")
}