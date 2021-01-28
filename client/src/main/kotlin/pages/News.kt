package pages

import kotlinx.coroutines.CoroutineScope
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.router.dom.navLink
import styled.styledDiv
import styled.styledH1

external interface NewsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedNewsId: String
}

class News : RComponent<NewsProps, RState>() {
    override fun RBuilder.render() {
        styledH1 {
            +"Новости"
        }
        styledDiv {
            +props.selectedNewsId
        }

        if (props.selectedNewsId == "feed") {
            navLink<NewsProps>(to = "/news/1") {
                +"новость 1"
            }

        } else {

            val previousNewsId = if (props.selectedNewsId.toInt() > 0) {
                props.selectedNewsId.toInt() - 1
            } else {
                props.selectedNewsId.toInt()
            }
            val nextNewsId = props.selectedNewsId.toInt() + 1

            navLink<NewsProps>(to = "/news/$previousNewsId") {
                +"Предыдущая новость"
            }
            navLink<NewsProps>(to = "/news/feed"){
                +"Вернуться к ленте"
            }
            navLink<NewsProps>(to = "/news/$nextNewsId") {
                +"Следующая новость"
            }
        }
    }
}