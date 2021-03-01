package Consts

import adminPageComponents.Input

val gameInputs = mutableMapOf(
    "date" to Input("Дата", "date", isNecessary = false),
    "time" to Input("Время", "time", isNecessary = false),
    "teamA" to Input(
        "Команда А",
        "teamAId",
        isSelect = true,
        options = mapOf(),
        allowOtherOption = true
    ),
    "teamB" to Input(
        "Команда Б",
        "teamBId",
        isSelect = true,
        options = mapOf(),
        allowOtherOption = true
    ),
    "stadium" to Input("Стадион", "stadium", isNecessary = false),
    "result" to Input("Результат", "result", isNecessary = false),
)

val  recruitmentInputs = mutableMapOf(
    "dates" to Input("Желаемые даты просмотровых сборов", "dates"),
    "name" to Input("Ф.И.О. хоккеиста", "name"),
    "birthday" to Input("Дата рождения  (дд.мм.гг.)", "birthday"),
    "role" to Input(
        "Амплуа",
        "role",
        isSelect = true,
        options = roleMap
    ),
    "stickGrip" to Input("Хват клюшки", "stickGrip"),
    "params" to Input("Рост - Вес", "params"),
    "previousSchool" to Input("Хоккейная школа в предыдущем сезоне", "previousSchool"),
    "city" to Input("Место жительства (город)", "city"),
    "phone" to Input("Контактный телефон", "phone"),
    "email" to Input("E-mail", "email")
)

val teamMemberInputs = mutableMapOf(
    "number" to Input("Номер", "number"),
    "firstName" to Input("Имя", "firstName"),
    "lastName" to Input("Фамилия", "lastName"),
    "role" to Input(
        "Амплуа", "role", isSelect = true,
        options = roleMap,
        allowOtherOption = true
    ),
    "birthday" to Input("Дата рождения", "birthday", isNecessary = false),
    "city" to Input("Город", "city", isNecessary = false),
    "teamRole" to Input(
        "к/а",
        "teamRole",
        isSelect = true,
        options = mapOf("к" to "к", "а" to "а", "null" to "нет")
    )
)

val trainerInputs = mutableMapOf(
    "name" to Input("ФИО", "name"),
    "info" to Input("Информация", "info"),
)

val teamInputs = mutableMapOf(
    "teamName" to Input("Название команды", "teamName"),
    "teamLink" to Input("Ссылка", "teamLink"),
    "year" to Input("Год рождения участников", "year"),
    "name" to Input("ФИО тренера", "name"),
    "info" to Input("Информация о тренере", "info", isNecessary = false),
)

val workoutsInputs = mutableMapOf(
    "startDatetime" to Input("Начало тренировки", "startDatetime", isDateTime = true),
    "endDatetime" to Input("Конец", "endDatetime", isDateTime = true),
    "text" to Input("Текст", "text"),
)