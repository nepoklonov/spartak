data class UserSession(
    val level: Level
)

enum class Level(val lvl: Int, val password: String?) {
    SysAdmin(80, "adminispartak5928"),
    Admin(20, "the best club"),
    Moderator(5, "spartak-2021"),
    JustSomeone(1, null);

    infix fun hasRights(other: Level) = lvl >= other.lvl
}
