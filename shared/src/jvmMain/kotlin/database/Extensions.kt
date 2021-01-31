package database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.util.*

val properties = Properties().apply {
    load(File("local.properties").inputStream())
}

fun <T> database(statement: Transaction.() -> T): T {
    Database.connect(
        url = "jdbc:postgresql://127.0.0.1/spartak",
        driver = "org.postgresql.Driver",
        user = properties["user"] as? String ?: error( "invalid postgres user"),
        password = properties["password"] as? String ?: error( "invalid postgres password")
    )

    return transaction {
        addLogger(StdOutSqlLogger)
        statement()
    }
}