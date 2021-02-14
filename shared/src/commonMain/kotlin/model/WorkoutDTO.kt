package model

import kotlinx.serialization.Serializable

@Serializable
data class WorkoutDTO(
        var id: Int?,
        val startTime: String,
        val endTime: String,
        val dayOfWeek: Int,
        val sectionLink: String,
        val text: String,
        val actualFromDate: Double,
        val actualToDate: Double,
)