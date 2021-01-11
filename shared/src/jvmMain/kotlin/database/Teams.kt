package database

import org.jetbrains.exposed.dao.id.IntIdTable

object Teams : IntIdTable() {
    val name = text("name")
    val isOur = bool("isOur")
    val type = text("type")
    val trainerId = integer("trainerId")
}
