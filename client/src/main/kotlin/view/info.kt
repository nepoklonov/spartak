package view

import kotlinx.css.Color

enum class TextWithIcon(val header: String, val iconSrc: String, val text: String, val isLinked: Boolean) {
    Phone("Телефон:", "/images/phone-call.png", "8-999-064-73-63", false),
    Vk("Мы в соц. сетях:", "/images/vk.png", "https://vk.com/mhkspartakspb", true),
    Inst("Мы в соц. сетях:", "/images/instagram.png", "https://www.instagram.com/mhk_spartak/", true),
    Mail("Электронная почта:", "/images/email.png", "mhk.spartak.spb@gmail.com", false),
    Address("Адрес:", "/images/address.png", "Россия, г. Санкт-Петербург, Суздальский проспект 29 лит А", false),
}

enum class ColorSpartak(val color: Color){
    Red(Color("#9D0707")),
    Grey(Color("484444"))
}



