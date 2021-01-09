package model

//import java.time.LocalDate
//var date = LocalDate.parse("2018-12-12")

data class Workout(
        val date: String,
        val time: String,
        val team: Nothing,
        val type: String,// нападающие/маневренное катание/инд. работа
        val place: String, // лёд/земля/зал/игра дома -- у них много всякого
)