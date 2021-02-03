package database

import org.jetbrains.exposed.dao.id.IntIdTable

object Recruitment : IntIdTable() {
    val dates = text("dates")
    val name = text("name")
    val birthday = text("birthday")
    val role = text("role")
    val stickGrip = text("stickGrip")
    val params = text("params")
    val previousSchool = text("previousSchool")
    val city = text("city")
    val phone = text("phone")
    val email = text("email")
}