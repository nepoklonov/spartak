package pages

import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.border
import org.w3c.dom.get
import react.*
import react.dom.InnerHTML
import services.HtmlService
import services.NewsService
import styled.css
import styled.styledDiv
import styled.styledH3
import styled.styledP

external interface NewsLineProps : RProps {
    var coroutineScope: CoroutineScope
}

class NewsLineState: RState {
    var error: Throwable? = null
    val news: MutableList<ShortNews> = mutableListOf()
}
class NewsLine : RComponent<NewsLineProps, NewsLineState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext


    init {
        state = NewsLineState()
    }

    override fun componentDidMount() {
        val htmlService = HtmlService(coroutineContext)
        val newsService = NewsService(coroutineContext)
        props.coroutineScope.launch {
            for (i in 1..6) {
                val newsHtml = try {
                    htmlService.getHtml(newsService.getNewsById(i))
                } catch (e: Throwable) {
                    setState {
                        error = e
                    }
                    return@launch
                }
                console.log(newsHtml)
                val e = document.createElement("div")
                e.innerHTML = newsHtml
                val img = e.getElementsByTagName("img")[0]
                val h3 = e.getElementsByTagName("h3")[0]
                val p = e.getElementsByTagName("p")[0]
                setState {
                    news.add(ShortNews(h3?.innerHTML, img?.getAttribute("src"), p?.innerHTML))
                }
            }
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            state.news.forEach {
                styledDiv {
                    css {
                        backgroundImage = Image("url('${it.imageSrc}')")
                        backgroundSize = "cover"
                        height = 600.px
                        width = 20.pct
                        position = Position.relative
                    }
                }
                styledDiv {
                    styledH3 {
                        css { alignSelf = Align.center }
                        +it.header!!
                    }
                    styledP {
                        +it.content!!
                    }
                    css {
                        position = Position.relative
                        bottom = (-50).pct
                        height = 280.px
                        color = Color.white
                        width = 90.pct
                    }
                }



            }
            css{
                display = Display.flex
                justifyContent = JustifyContent.spaceBetween
            }
        }

    }
}

