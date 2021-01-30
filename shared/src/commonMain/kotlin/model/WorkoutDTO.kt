package model

import kotlinx.serialization.Serializable

//import java.time.LocalDate
//var date = LocalDate.parse("2018-12-12")

@Serializable
data class WorkoutDTO(
        var id: Int?,
        val datetime: Double,
        val teamId: Int,
        val type: String,// нападающие/маневренное катание/инд. работа
        val place: String, // лёд/земля/зал/игра дома -- у них много всякого
)