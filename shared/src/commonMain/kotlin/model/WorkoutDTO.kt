package model

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutDTO(
        var id: Int?,
        val datetime: Double,
        val teamLink: String,
        val type: String,
        val place: String,
)