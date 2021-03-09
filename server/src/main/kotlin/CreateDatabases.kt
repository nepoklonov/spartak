import database.*
import org.jetbrains.exposed.sql.SchemaUtils

fun createDatabases(){
    database {
        SchemaUtils.create(TeamMembers)
        SchemaUtils.create(Games)
        SchemaUtils.create(Trainers)
        SchemaUtils.create(Photos)
        SchemaUtils.create(Teams)
        SchemaUtils.create(Workouts)
        SchemaUtils.create(Admins)
        SchemaUtils.create(Recruitment)
        SchemaUtils.create(News)
        SchemaUtils.create(GallerySections)
        SchemaUtils.create(GamesSections)
        SchemaUtils.create(WorkoutsSections)
        SchemaUtils.create(Products)
    }
}