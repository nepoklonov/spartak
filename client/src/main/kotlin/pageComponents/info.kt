package pageComponents

import kotlinx.css.Color


//TODO: файл с маленькой буквы
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



