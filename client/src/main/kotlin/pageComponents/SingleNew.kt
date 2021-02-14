package view

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import pageComponents.ButtonSecondary
import react.*
import react.dom.InnerHTML
import react.router.dom.navLink
import services.HtmlService
import services.NewsService
import styled.*
import kotlin.js.Date

external interface NewsProps : RProps {
    var coroutineScope: CoroutineScope
    var selectedNewsId: Int
}

class NewsState: RState {
    var error: Throwable? = null
    var longNews: LongNews? = null
}
data class LongNews(
        var news: String?,
        var date: Long,
        var prevId: Int?,
        var nextId: Int?
)

class SingleNew : RComponent<NewsProps, NewsState>() {
    private val coroutineContext
        get() = props.coroutineScope.coroutineContext


        init {
            state = NewsState()
        }
    private fun setHTML(){
        props.coroutineScope.launch {
            val htmlService = HtmlService(coroutineContext)
            val newsService = NewsService(coroutineContext)
            var newsHtml: LongNews
            try {
                newsHtml = newsService.getNewsTripleById(props.selectedNewsId.toInt()).let{
                    LongNews(htmlService.getHtml(it.url), it.date, it.prevId, it.nextId)
                }
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }
            setState {
                longNews = newsHtml
            }
        }
    }
    override fun componentDidMount() {
        setHTML()
    }

    override fun componentDidUpdate(prevProps: NewsProps, prevState: NewsState, snapshot: Any) {
        if (prevProps != props) setHTML()
    }



    override fun RBuilder.render() {
        val previousNewsId = if (state.longNews?.prevId != null) {
            state.longNews?.prevId
        } else {
            props.selectedNewsId
        }
        val nextNewsId = if (state.longNews?.nextId != null) {
            state.longNews?.nextId
        } else {
            props.selectedNewsId
        }
        styledDiv {
            styledDiv {
                css{
                    overflow = Overflow.hidden
                }
                if (state.longNews?.news != null) {
                    attrs["dangerouslySetInnerHTML"] = InnerHTML(state.longNews?.news!!)
//                    styledH5 {
//                        +Date(state.longNews?.date!!).getDate().toString()
//                          +"."
//                        +(Date(state.longNews?.date!!).getMonth()+1).toString()
//                          +"."
//                        +Date(state.longNews?.date!!).getFullYear().toString()
//                     }
                } else {
                    +"загрузка..."
                }
            }
            styledDiv {
                css {
                    display = Display.flex
                    justifyContent = JustifyContent.spaceAround
                }

                navLink<pages.NewsProps>(to = "/news/$previousNewsId") {
                    child(ButtonSecondary::class) {
                        attrs.text = "Предыдущая новость"
                    }
                }
                navLink<pages.NewsProps>(to = "/news/feed") {
                    child(ButtonSecondary::class) {
                        attrs.text = "Вернуться к ленте"
                    }
                }
                navLink<pages.NewsProps>(to = "/news/$nextNewsId") {
                    child(ButtonSecondary::class) {
                        attrs.text = "Следующая новость"
                    }
                }
            }
        }
}
    }

