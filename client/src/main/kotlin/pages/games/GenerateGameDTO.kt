package pages.games

import consts.Input
import model.GameDTO

fun generateGameDTO(inputs: MutableMap<String, Input>, id: Int?, selectedChampionship: String): GameDTO {
    return GameDTO(
        id,
        inputs["date"]!!.inputValue,
        inputs["time"]!!.inputValue,
        selectedChampionship,
        inputs["teamAId"]!!.inputValue.toInt(),
        inputs["teamBId"]!!.inputValue.toInt(),
        inputs["stadium"]!!.inputValue,
        inputs["result"]!!.inputValue,
    )
}