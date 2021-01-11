package database

import org.jetbrains.exposed.dao.id.IntIdTable

object Trainers : IntIdTable() {
    val name = text("name")
    val dateOfBirth = text("dateOfBirth")
    val info = text("info")
}