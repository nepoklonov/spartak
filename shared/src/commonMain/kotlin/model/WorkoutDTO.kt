package model

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutDTO(
        var id: Int?,
        val datetime: Double,
        val sectionLink: String,
        val text: String,
)