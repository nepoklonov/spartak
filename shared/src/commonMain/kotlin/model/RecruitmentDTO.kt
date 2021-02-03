package model

import kotlinx.serialization.Serializable

@Serializable
data class RecruitmentDTO(
    var id: Int?,
    val dates: String,
    var name: String,
    val birthday: String,
    val role: String,
    val stickGrip: String,
    val params: String,
    val previousSchool: String,
    val city: String,
    val phone: String,
    val email: String,
)