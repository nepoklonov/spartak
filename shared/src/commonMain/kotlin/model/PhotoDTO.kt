package model

import kotlinx.serialization.Serializable

@Serializable
data class PhotoDTO(
    var id: Int?,
    val url: String,
    val gallerySection: String
)