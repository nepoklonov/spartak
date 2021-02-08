package database

import org.jetbrains.exposed.dao.id.IntIdTable

object WorkoutsSections: IntIdTable() {
    val name = text("name")
    val link = text("link")
}