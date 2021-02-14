package database

import org.jetbrains.exposed.dao.id.IntIdTable

object Workouts : IntIdTable() {
    val startTime = text("startTime")
    val endTime = text("endTime")
    val dayOfWeek = integer("dayOfWeek")
    val sectionLink = text("sectionLink")
    val text = text("text")
    val actualFromDate = double("actualFromDate")
    val actualToDate = double("actualToDate")
}