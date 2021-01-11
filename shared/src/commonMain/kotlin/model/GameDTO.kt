package model

data class GameDTO(
        var id: Int?,
        val date: String,
        val time: String?,
        val teamAId: Int,
        val teamBId: Int,
        val stadium: String,
        val result: String?,
)