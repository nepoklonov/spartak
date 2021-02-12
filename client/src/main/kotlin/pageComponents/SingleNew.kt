package view

import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import org.w3c.dom.get
import react.*
import react.dom.InnerHTML
import react.router.dom.navLink
import services.HtmlService
import services.NewsService
import styled.*
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
        var prevId: Int?,
        var nextId: Int?
)

class SingleNew : RComponent<NewsProps, NewsState>() {
    private val coroutineContext
        get() = props.coroutineScope.coroutineContext


        init {
            state = NewsState()
        }
        override fun componentDidMount() {
            props.coroutineScope.launch {
                val htmlService = HtmlService(coroutineContext)
                val newsService = NewsService(coroutineContext)
                var newsHtml: LongNews
                try {
                    newsHtml = newsService.getNewsTripleById(props.selectedNewsId.toInt()).let{
                        LongNews(htmlService.getHtml(it.url), it.prevId, it.nextId)
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
            if (state.longNews?.news != null) {
                attrs["dangerouslySetInnerHTML"] = InnerHTML(state.longNews?.news!!)
            } else {
                +"загрузка..."
            }
        }
        styledDiv {
            css{
                display = Display.flex
                justifyContent = JustifyContent.spaceAround
            }

            navLink<pages.NewsProps>(to = "/news/$previousNewsId") {
                child(ButtonSecondary::class)  {
                    attrs.text = "Предыдущая новость"
                }
            }
            navLink<pages.NewsProps>(to = "/news/feed") {
                child(ButtonSecondary::class)  {
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
