package services

import database.Games
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
            Games.select { Games.year eq year }.forEach() {
                listOfGamesByYear += GameDTO(
                        it[Games.id].value,
                        it[Games.date],
                        it[Games.time],
                        it[Games.year],
                        it[Games.teamAId],
                        it[Games.teamBId],
                        it[Games.stadium],
                        it[Games.result])
            }
        }
        return listOfGamesByYear
    }

    private fun Games.insertGameDtoToDatabase(it: UpdateBuilder<Int>, newGame:GameDTO){
        it[date] = newGame.date
        it[time] = newGame.time.toString()
        it[year] = newGame.year
        it[teamAId] = newGame.teamAId
        it[teamBId] = newGame.teamBId
        it[stadium] = newGame.stadium
        it[result] = newGame.result.toString()
    }

//    @RequireRole(Role.Admin)
    actual suspend fun addGame(newGame: GameDTO): Int {
        return database {
            Games.insertAndGetId {
                insertGameDtoToDatabase(it, newGame)
            }
        }.value
    }

//    @RequireRole(Role.Admin)
    actual suspend fun editGame(game: GameDTO): Boolean {
        database {
            Games.update({ Games.id eq game.id }) {
                insertGameDtoToDatabase(it, game)
            }
        }
        return true
    }

//    @RequireRole(Role.Admin)
    actual suspend fun deleteGame(id: Int): Boolean {
        database {
            Games.deleteWhere{ Games.id eq id }
        }
        return true
    }
}