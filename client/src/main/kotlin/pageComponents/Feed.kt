package pages

import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import model.NewsDTO
import org.w3c.dom.get
import pageComponents.ButtonSecondary
import react.*
import services.HtmlService
import services.NewsService
import styled.*
import kotlin.js.Date

external interface FeedProps : RProps {
    var coroutineScope: CoroutineScope
}

class FeedState: RState {
    var error: Throwable? = null
    val news: MutableList<ShortNews> = mutableListOf()
}
class Feed : RComponent<FeedProps, FeedState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext


    init {
        state = FeedState()
    }

    override fun componentDidMount() {
        val htmlService = HtmlService(coroutineContext)
        val newsService = NewsService(coroutineContext)
        props.coroutineScope.launch {
            val newsHtml : MutableList<NewsDTO> = mutableListOf()
            try {
                newsService.getLastNews().forEach { newsHtml.add(NewsDTO(it.id, htmlService.getHtml(it.url), it.date)) }
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }
            for (new in newsHtml) {
                val e = document.createElement("div")
                e.innerHTML = new.url
                val img = e.getElementsByTagName("img")[0]
                val h3 = e.getElementsByTagName("h3")[0]
                val p = e.getElementsByTagName("p")[0]?.innerHTML + e.getElementsByTagName("p")[1]?.innerHTML
                setState {
                    news.add(ShortNews(h3?.innerHTML, img?.getAttribute("src"), p, new.id, new.date))
                }
            }
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            state.news.forEachIndexed { i, it ->

                styledDiv {
                    styledDiv {
                        css {
                            backgroundImage = Image("url('${it.imageSrc}')")
                            backgroundSize = "cover"
                            if (i%2 == 0) float = Float.left
                            else float = Float.right
                            height = 90.pct
                            width = 40.pct
                            display = Display.flex
                            margin = 1.pct.toString()
                        }
                    }
                    styledDiv {
                        styledH3 {
                            +it.header!!
                        }
                        styledH5 {
                            +Date(it.date).getDate().toString()
                            +"."
                            +(Date(it.date).getMonth()+1).toString()
                            +"."
                            +Date(it.date).getFullYear().toString()
                        }
                        styledP {
                            +it.content!!
                        }
                        css {
                            if (i % 2 == 0) float = Float.right
                            else float = Float.left
                            width = 50.pct
                            height = 450.px
                            margin = 1.pct.toString()
                        }
                        styledA(href = "/news/${it.id}"){
                            child(ButtonSecondary::class) {
                                attrs.text = "Читать далее"
                            }
                        }
                    }
                    css {
                        marginBottom = 20.px
                        height = 450.px
                        display = Display.inlineBlock
                        justifyContent = JustifyContent.spaceBetween

                    }
                }
            }
        }

    }
}

