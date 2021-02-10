package model

import kotlinx.serialization.Serializable

@Serializable
data class TrainerDTO(
        var id: Int?,
        val teamLink: String,
        val photo: String,
        val name: String,
        val dateOfBirth: String,
        val info: String,
)