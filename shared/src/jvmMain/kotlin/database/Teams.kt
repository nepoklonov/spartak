package database

import org.jetbrains.exposed.dao.id.IntIdTable

object Teams : IntIdTable() {
    val teamId = integer("teamId")
    val teamName = text("teamName")
    val trainerId = text("trainerId")
}
