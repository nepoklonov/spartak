package model

import kotlinx.serialization.Serializable

@Serializable
data class GameDTO(
        var id: Int?,
        val date: String,
        val time: String?,
        val year: Int,
        val teamAId: Int,
        val teamBId: Int,
        val stadium: String,
        val result: String?,
)