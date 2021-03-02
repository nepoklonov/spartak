package pages.games

import adminPageComponents.Input
import model.GameDTO

fun generateGameDTO(inputs: MutableMap<String, Input>, id: Int?, selectedChampionship: String): GameDTO {
    return GameDTO(
        id,
        inputs["date"]!!.inputValue,
        inputs["time"]!!.inputValue,
        selectedChampionship,
        inputs["teamA"]!!.inputValue.toInt(),
        inputs["teamB"]!!.inputValue.toInt(),
        inputs["stadium"]!!.inputValue,
        inputs["result"]!!.inputValue,
    )
}