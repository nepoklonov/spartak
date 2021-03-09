package model

import kotlinx.serialization.Serializable

@Serializable
data class ProductDTO(
    var id: Int?,
    val name: String,
    val cost: String,
    val photo: String
)