package model
import kotlinx.serialization.Serializable

@Serializable
data class NewsTripleDTO(
        var id: Int?,
        val url: String,
        val date: Long,
        val prevId: Int?,
        val nextId: Int?
)