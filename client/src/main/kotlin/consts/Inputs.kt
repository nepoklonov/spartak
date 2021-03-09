package consts

data class Input(
    val header: String,
    val inputName: String,
    var inputValue: String = "",
    var isRed: Boolean = false,
    val isSelect: Boolean = false,
    var options: Map<String, String> = mapOf(),
    val allowOtherOption: Boolean = false,
    var otherOption: Boolean = false,
    val isDateTime: Boolean = false,
    val isNecessary: Boolean = true,
    val isFile: Boolean = false
)

val productInputs = listOf(
    Input("Название товара", "name"),
    Input("Стоимость", "cost"),
    Input("Фото", "photo", isFile = true)
).associateBy { it.inputName }.toMutableMap()

val galleryInputs = listOf(
    Input("Фото", "photo", isFile = true),
).associateBy { it.inputName }.toMutableMap()

val navigationInputs = listOf(
    Input("Название раздела", "sectionName"),
    Input("Ссылка", "sectionLink")
).associateBy { it.inputName }.toMutableMap()

val gameInputs = listOf(
    Input("Дата", "date", isNecessary = false),
    Input("Время", "time", isNecessary = false),
    Input(
        "Команда А",
        "teamAId",
        isSelect = true,
        options = mapOf(),
        allowOtherOption = true
    ),
    Input(
        "Команда Б",
        "teamBId",
        isSelect = true,
        options = mapOf(),
        allowOtherOption = true
    ),
    Input("Стадион", "stadium", isNecessary = false),
    Input("Результат", "result", isNecessary = false),
).associateBy { it.inputName }.toMutableMap()

val recruitmentInputs = listOf(
    Input("Желаемые даты просмотровых сборов", "dates"),
    Input("Ф.И.О. хоккеиста", "name"),
    Input("Дата рождения  (дд.мм.гг.)", "birthday"),
    Input(
        "Амплуа",
        "role",
        isSelect = true,
        options = mapOf("defenders" to "Защитники", "strikers" to "Нападающие", "goalkeepers" to "Вратари")
    ),
    Input("Хват клюшки", "stickGrip"),
    Input("Рост - Вес", "params"),
    Input("Хоккейная школа в предыдущем сезоне", "previousSchool"),
    Input("Место жительства (город)", "city"),
    Input("Контактный телефон", "phone"),
    Input("E-mail", "email")
).associateBy { it.inputName }.toMutableMap()

val teamMemberInputs = listOf(
    Input("Номер", "number"),
    Input("Имя", "firstName"),
    Input("Фамилия", "lastName"),
    Input("Фото", "photo", isFile = true),
    Input(
        "Амплуа", "role", isSelect = true,
        options = roleMap
    ),
    Input("Дата рождения", "birthday", isNecessary = false),
    Input("Город", "city", isNecessary = false),
    Input(
        "к/а",
        "teamRole",
        isSelect = true,
        options = mapOf("к" to "к", "а" to "а", "null" to "нет")
    )
).associateBy { it.inputName }.toMutableMap()

val trainerInputs = listOf(
    Input("ФИО", "name"),
    Input("Фото", "photo", isFile = true),
    Input("Информация", "info"),
).associateBy { it.inputName }.toMutableMap()

val teamInputs = listOf(
    Input("Название команды", "teamName"),
    Input("Ссылка", "teamLink"),
    Input("Год рождения участников", "year"),
    Input("ФИО тренера", "name"),
    Input("Информация о тренере", "info", isNecessary = false),
).associateBy { it.inputName }.toMutableMap()

val workoutsInputs = listOf(
    Input("Начало тренировки", "startDatetime", isDateTime = true),
    Input("Конец", "endDatetime", isDateTime = true),
    Input("Текст", "text"),
).associateBy { it.inputName }.toMutableMap()