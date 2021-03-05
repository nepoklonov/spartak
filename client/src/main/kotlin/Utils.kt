import kotlinx.browser.document
import react.RBuilder

val isAdmin = document.cookie.contains("role=admin")

fun RBuilder.loading(){
    +"Загрузка..."
}