package model

import kotlinx.serialization.Serializable

@Serializable
data class PhotoDTO(
    val url: String,
    val gallerySection: String
)