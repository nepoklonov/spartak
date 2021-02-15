import java.text.SimpleDateFormat
import java.util.*

fun createTimestamp(pattern: String): String {
    val date = Date()
    val dateFormat = SimpleDateFormat(pattern)
    return dateFormat.format(date)
}

fun randomString(length: Long): String {
    val leftLimit = 97 // letter 'a'
    val rightLimit = 122 // letter 'z'
    return Random().ints(leftLimit, rightLimit + 1)
        .limit(length)
        .collect({ StringBuilder() }, java.lang.StringBuilder::appendCodePoint, java.lang.StringBuilder::append)
        .toString()
}