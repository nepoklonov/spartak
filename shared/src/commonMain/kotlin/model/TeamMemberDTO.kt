package model

import kotlinx.serialization.Serializable

@Serializable
data class TeamMemberDTO(
        var id: Int?,
        val teamLink: String,
        val photo: String,
        val firstName: String,
        val lastName: String,
        val role: String,
        val birthday: String,
        val city: String,
)