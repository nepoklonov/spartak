package database

import org.jetbrains.exposed.dao.id.IntIdTable

object WorkoutsSections: IntIdTable() {
    val header = text("name")
    val link = text("link")
}