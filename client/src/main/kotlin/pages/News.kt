package pages

import greyButtonSpartak
import headerText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.Display
import kotlinx.css.JustifyContent
import kotlinx.css.display
import kotlinx.css.justifyContent
import react.*
import react.dom.InnerHTML
import react.router.dom.navLink
import services.HtmlService
import services.NewsService
import styled.css
import styled.styledDiv
import view.ButtonSecondary
import view.SingleNew

external interface NewsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedNewsId: String
}

class News : RComponent<NewsProps, RState>() {
    private val coroutineContext
        get() = props.coroutineScope.coroutineContext


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
            child(SingleNew::class){
                attrs.coroutineScope = props.coroutineScope
                attrs.selectedNewsId = props.selectedNewsId.toInt()
            }


        }
    }
}



