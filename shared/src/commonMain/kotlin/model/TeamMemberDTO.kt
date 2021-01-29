package model

import kotlinx.serialization.Serializable

@Serializable
data class TeamMemberDTO(
        var id: Int?,
        val teamId: Int,
        val photo: String,
        val firstName: String,
        val lastName: String,
        val role: String,
        val birthday: String,
        val city: String,
)