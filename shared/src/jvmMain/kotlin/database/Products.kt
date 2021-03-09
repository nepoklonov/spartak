package database

import org.jetbrains.exposed.dao.id.IntIdTable

object Products: IntIdTable() {
    val name = text("name")
    val cost = text("cost")
    val photo = text("photo")
}