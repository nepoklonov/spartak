package model

data class Game(
        val id: Int,
        val date: String,
        val time: String?,
        val ourTeam: String,// TODO: подвязать команды
        val opposingTeam: String,
        val isOurTeamFirst: Boolean,
        val stadium: String,
        val result: String?,
)