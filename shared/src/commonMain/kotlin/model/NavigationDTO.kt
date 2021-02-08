package model

import kotlinx.serialization.Serializable

@Serializable
class NavigationDTO(
    var id: Int?,
    val header: String,
    val link: String
)