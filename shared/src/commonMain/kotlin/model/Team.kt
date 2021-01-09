package model

import kotlinx.serialization.Serializable

@Serializable
data class Team(
        val teamId: Int, val teamName: String, val trainerId: String
)
