package model

import kotlinx.serialization.Serializable

@Serializable
data class TeamMemberDTO(
        var id: Int?,
        val teamLink: String,
        val number: String,
        val photo: String,
        val firstName: String,
        val lastName: String,
        val role: String,
        val birthday: String,
        val city: String,
        val teamRole: String //к/а это типа капитан/ а...?
)