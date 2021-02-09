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

external interface MainNewsProps : RProps {
    var coroutineScope: CoroutineScope
}

data class ShortNews(
        val header: String?,
        val imageSrc: String?,
        val content: String?
)

class MainNewsState : RState {
    var error: Throwable? = null
    val news: MutableList<ShortNews> = mutableListOf()
}

class MainNews : RComponent<MainNewsProps, MainNewsState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext


    init {
        state = MainNewsState()
    }

    override fun componentDidMount() {
        val htmlService = HtmlService(coroutineContext)
        val newsService = NewsService(coroutineContext)
        props.coroutineScope.launch {
                val newsHtml : MutableList<String> = mutableListOf()
                try {
                    newsService.getLastNews(4).forEach { newsHtml.add(htmlService.getHtml(it.url)) }
                } catch (e: Throwable) {
                    setState {
                        error = e
                    }
                    return@launch
                }
            for (new in newsHtml) {
                val e = document.createElement("div")
                e.innerHTML = new
                val img = e.getElementsByTagName("img")[0]
                val h3 = e.getElementsByTagName("h3")[0]
                val p = e.getElementsByTagName("p")[0]
                setState {
                    news.add(ShortNews(h3?.innerHTML, img?.getAttribute("src"), p?.innerHTML?.substring(0, 200)))
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
                    styledDiv {
                        css {
                            height = 98.pct
                            background = "linear-gradient(180deg, rgba(255, 255, 255, 0) 0%, rgba(0, 0, 0, 0.71) 41.67%)"
                        }
                        styledDiv {
                            styledH3 {
                                css {
                                    alignSelf = Align.center
                                }
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
                                margin = 5.px.toString()
                            }
                        }
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

