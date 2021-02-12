package database

import org.jetbrains.exposed.dao.id.IntIdTable

object Workouts : IntIdTable() {
    val datetime = double("datetime")
    val sectionLink = text("sectionLink")
    val text = text("text")
}