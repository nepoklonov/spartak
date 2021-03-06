package pages

import header
import kotlinx.coroutines.CoroutineScope
import pageComponents.Feed
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import pageComponents.SingleNew

external interface NewsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedNewsId: String
}

class News : RComponent<NewsProps, RState>() {
    override fun RBuilder.render() {

        header { +"Новости" }

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



