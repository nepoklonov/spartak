package database

import org.jetbrains.exposed.dao.id.IntIdTable

object Games : IntIdTable() {
    val date = text("date")
    val time = text("time")
    val year = text("year")
    val teamAId = integer("teamAId")
    val teamBId = integer("teamBId")
    val stadium = text("stadium")
    val result = text("result")
}