package model

import kotlinx.serialization.Serializable

@Serializable
data class TeamDTO(
        var id: Int?,
        val name: String,
        val isOur: Boolean,
        val year: String?,
        val trainerId: Int?,
)
