package model

import kotlinx.serialization.Serializable

@Serializable
data class TeamMemberDTO(
        var id: Int?,
        val teamId: Int,
        //val photo: ???,
        val firstName: String,
        val lastName: String,
        // че такое к/а
        val role: String,
        val birthday: String,
        val city: String,
)