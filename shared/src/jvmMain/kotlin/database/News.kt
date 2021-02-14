package database

import org.jetbrains.exposed.dao.id.IntIdTable

object News: IntIdTable()  {
    val url = text("url")
    val date = long("date")
}