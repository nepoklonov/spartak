package model
import kotlinx.serialization.Serializable

@Serializable
class NewsDTO (
        var id: Int?,
        val url: String,
        val date: Long
)