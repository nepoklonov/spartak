package database

import org.jetbrains.exposed.dao.id.IntIdTable

object Timetable : IntIdTable() {
    val datetime = double("datetime")
    val teamLink = text("teamLink")
    val type = text("type")
    val place = text("place")
}