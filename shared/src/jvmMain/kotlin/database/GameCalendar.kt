package database

import org.jetbrains.exposed.dao.id.IntIdTable

object GameCalendar : IntIdTable() {
    val date = text("date")
    val time = text("time")
    val teamAId = integer("teamAId")
    val teamBId = integer("teamBId")
    val stadium = text("stadium")
    val result = text("result")
}