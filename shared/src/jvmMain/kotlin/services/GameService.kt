package services

import database.GameCalendar
import database.database
import model.GameDTO
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update
import rpc.RPCService

actual class GameService: RPCService {
    actual suspend fun getAllGamesByYear(year: String): List<GameDTO> {
        val listOfGamesByYear: MutableList<GameDTO> = mutableListOf()
        database {
            GameCalendar.select { GameCalendar.year eq year }.forEach() {
                listOfGamesByYear += GameDTO(
                        it[GameCalendar.id].value,
                        it[GameCalendar.date],
                        it[GameCalendar.time],
                        it[GameCalendar.year],
                        it[GameCalendar.teamAId],
                        it[GameCalendar.teamBId],
                        it[GameCalendar.stadium],
                        it[GameCalendar.result])
            }
        }
        return listOfGamesByYear
    }

    private fun GameCalendar.insertGameDtoToDatabase(it: UpdateBuilder<Int>, newGame:GameDTO){
        it[date] = newGame.date
        it[time] = newGame.time.toString()
        it[year] = newGame.year
        it[teamAId] = newGame.teamAId
        it[teamBId] = newGame.teamBId
        it[stadium] = newGame.stadium
        it[result] = newGame.result.toString()
    }

    actual suspend fun addGame(newGame: GameDTO): Int {
        return database {
            GameCalendar.insertAndGetId {
                insertGameDtoToDatabase(it, newGame)
            }
        }.value
    }

    actual suspend fun editGame(game: GameDTO): Boolean {
        database {
            GameCalendar.update({ GameCalendar.id eq game.id }) {
                insertGameDtoToDatabase(it, game)
            }
        }
        return true
    }

    actual suspend fun deleteGame(id: Int): Boolean {
        database {
            GameCalendar.deleteWhere{ GameCalendar.id eq id }
        }
        return true
    }
}