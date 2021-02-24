package pageComponents

import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import adminPageComponents.CKEditorComponent
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


        init {
            state = NewsState()
        }
    private fun setHTML(){
        props.coroutineScope.launch {
            val htmlService = HtmlService(coroutineContext)
            val newsService = NewsService(coroutineContext)
            val newsHtml = newsService.getNewsTripleById(props.selectedNewsId).let{
                    LongNews(htmlService.getHtml(it.url), it.date, it.prevId, it.nextId)
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
        if (document.cookie.contains("role=admin")&& (state.longNews?.news != null)) {
            child(CKEditorComponent::class) {
                attrs.text = state.longNews?.news!!
                attrs.coroutineScope = props.coroutineScope
                attrs.url = "newsHtml/${props.selectedNewsId}.html"
            }
        }
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
            styledH5 {
                if (state.longNews?.date !=null){
                    val date = state.longNews?.date!!
                    console.log(date)
                    +Date(date).getDate().toString()
                    +"."
                    +(Date(date).getMonth()+1).toString()
                    +"."
                    +Date(date).getFullYear().toString()
                    css {
                        paddingLeft = 50.px
                    }
                }
            }
            styledDiv {
                css{
                    overflow = Overflow.hidden
                }
                if (state.longNews?.news != null) {
                    attrs["dangerouslySetInnerHTML"] = InnerHTML(state.longNews?.news!!)
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
                    buttonSecondary("Предыдущая новость")
                }
                navLink<pages.NewsProps>(to = "/news/feed") {
                    buttonSecondary("Вернуться к ленте")
                }
                navLink<pages.NewsProps>(to = "/news/$nextNewsId") {
                    buttonSecondary("Следующая новость")
                }
            }
        }
}
    }

