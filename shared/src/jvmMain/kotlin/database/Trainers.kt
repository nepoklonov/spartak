package database

import org.jetbrains.exposed.dao.id.IntIdTable
import java.util.*

object Trainers : IntIdTable() {
    val trainerId = integer("trainerId")
    val trainerName = text("trainerName")
    val trainerDateOfBirth = text("trainerDateOfBirth")
    val trainerInfo = text("trainerInfo")
}