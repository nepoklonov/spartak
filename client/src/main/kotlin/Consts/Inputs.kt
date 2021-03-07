package Consts

import adminPageComponents.Input

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
        options = roleMap
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
    Input(
        "Амплуа", "role", isSelect = true,
        options = roleMap,
        allowOtherOption = true
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