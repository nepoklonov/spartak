package pages

import header
import kotlinx.coroutines.CoroutineScope
import react.*
import view.SingleNew

external interface NewsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedNewsId: String
}

class News : RComponent<NewsProps, RState>() {
    private val coroutineContext
        get() = props.coroutineScope.coroutineContext


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



