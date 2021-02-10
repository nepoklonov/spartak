package database

import org.jetbrains.exposed.dao.id.IntIdTable

object Trainers : IntIdTable() {
    val teamLink = text("teamLink")
    val name = text("name")
    val photo = text("photo")
    val dateOfBirth = text("dateOfBirth")
    val info = text("info")
}