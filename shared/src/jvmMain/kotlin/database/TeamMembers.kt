package database

import org.jetbrains.exposed.dao.id.IntIdTable

object TeamMembers : IntIdTable() {
    val teamId = integer("teamId")
    val firstName = text("firstName")
    val lastName = text("lastName")
    val role = text("role")
    val birthday = text("birthday")
    val city = text("city")
}
