package database

import org.jetbrains.exposed.dao.id.IntIdTable

object GameCalendar : IntIdTable() {
    val gameId = integer("id")
    val date = text("date")
    val time = text("time")
    val ourTeam = text("ourTeam")// TODO: подвязать команды
    val opposingTeam = text("opposingTeam")
    val isOurTeamFirst = bool("isOurTeamFirst")
    val stadium = text("stadium")
    val result = text("result")
}