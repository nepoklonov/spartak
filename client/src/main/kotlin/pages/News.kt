package pages

import greyButtonSpartak
import headerText
import kotlinx.coroutines.CoroutineScope
import kotlinx.css.Display
import kotlinx.css.JustifyContent
import kotlinx.css.display
import kotlinx.css.justifyContent
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.router.dom.navLink
import styled.css
import styled.styledDiv

external interface NewsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedNewsId: String
}

class News : RComponent<NewsProps, RState>() {
    override fun RBuilder.render() {

        headerText { +"Новости" }

        styledDiv {
            +props.selectedNewsId
        }

        if (props.selectedNewsId == "feed") {
            child(Feed::class){
                attrs.coroutineScope = props.coroutineScope
            }

        } else {

            val previousNewsId = if (props.selectedNewsId.toInt() > 0) {
                props.selectedNewsId.toInt() - 1
            } else {
                props.selectedNewsId.toInt()
            }
            val nextNewsId = props.selectedNewsId.toInt() + 1

            styledDiv {
                css{
                    display = Display.flex
                    justifyContent = JustifyContent.spaceAround
                }

                navLink<NewsProps>(to = "/news/$previousNewsId") {
                    greyButtonSpartak {
                        +"Предыдущая новость"
                    }
                }
                navLink<NewsProps>(to = "/news/feed") {
                    greyButtonSpartak {
                        +"Вернуться к ленте"
                    }
                }
                navLink<NewsProps>(to = "/news/$nextNewsId") {
                    greyButtonSpartak {
                        +"Следующая новость"
                    }
                }
            }
        }
    }
}