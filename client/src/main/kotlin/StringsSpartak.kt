import kotlinx.css.Color

enum class TextWithIcon(val header: String, val iconSrc: String, val text: String, val isLinked: Boolean) {
    Phone("Телефон:", "/images/phone-call.png", "8\u2011999\u2011064\u201173\u201163", false),
    Vk("Мы в соц. сетях:", "/images/vk.png", "https://vk.com/mhkspartakspb", true),
    Inst("Мы в соц. сетях:", "/images/instagram.png", "https://www.instagram.com/mhk_spartak/", true),
    Mail("Электронная почта:", "/images/email.png", "mhk.spartak.spb@gmail.com", false),
    Address("Адрес:", "/images/address.png", "Россия, г. Санкт\u2011Петербург, Суздальский проспект 29 лит А", false),
}

enum class ColorSpartak(val color: Color){
    Red(Color("#9D0707")),
    Grey(Color("#484444")),
    LightGrey(Color("#E0DEDF"))
}

val months = mapOf(
    0 to "января",
    1 to "февраля",
    2 to "марта",
    3 to "апреля",
    4 to "мая",
    5 to "июня",
    6 to "июля",
    7 to "августа",
    8 to "сентября",
    9 to "октября",
    10 to "ноября",
    11 to "декабря",
)

val daysOfWeek = mapOf(
    1 to "понедельник",
    2 to "вторник",
    3 to "среда",
    4 to "четверг",
    5 to "пятница",
    6 to "суббота",
    7 to "воскресенье"
)

val tableHeaders =
    mapOf(
        "date" to "Дата",
        "time" to "Время",
        "teamA" to "Команда А",
        "teamB" to "Команда Б",
        "stadium" to "Стадион",
        "result" to "Результат"
    )



