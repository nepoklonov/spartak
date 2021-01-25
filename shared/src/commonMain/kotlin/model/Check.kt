package model

import kotlinx.serialization.Serializable

@Serializable
data class Check(
    val checkId: Int, val checkText: String
)
