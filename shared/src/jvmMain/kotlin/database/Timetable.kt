package database

import org.jetbrains.exposed.dao.id.IntIdTable

object Timetable : IntIdTable() {
    val date = text("date")
    val time = text("time")
    val team = text("team") // сюда можно подвязать команды, а можно не подвязывать...
    // в расписании бывают всякие команды набора, а отдельных сраниц для них нет, соответственно и участников тоже
    val type = text("type")
    val place = text("place")
}