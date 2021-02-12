package pages

import greyButtonSpartak
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
import styled.*
import view.ButtonSecondary

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
            val newsHtml : MutableMap<String, Int?> = mutableMapOf()
            try {
                newsService.getLastNews().forEach { newsHtml.put(htmlService.getHtml(it.url), it.id) }
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }
            for (new in newsHtml) {
                val e = document.createElement("div")
                e.innerHTML = new.key
                val img = e.getElementsByTagName("img")[0]
                val h3 = e.getElementsByTagName("h3")[0]
                val p = e.getElementsByTagName("p")[0]?.innerHTML + e.getElementsByTagName("p")[1]?.innerHTML
                setState {
                    news.add(ShortNews(h3?.innerHTML, img?.getAttribute("src"), p, new.value))
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
                            width = 500.px
                            height = 400.px
                            margin = 30.px.toString()
                        }
                    }
                    styledDiv {
                        styledH3 {
                            +it.header!!
                        }
                        styledP {
                            +it.content!!
                        }
                        css {
                            if (i % 2 == 0) float = Float.right
                            else float = Float.left
                            width = 40.pct
                            height = 400.px
                            margin = 30.px.toString()
                        }
                        styledA(href = "/news/${it.id}"){
                            child(ButtonSecondary::class) {
                                attrs.text = "Читать далее"
                            }
                        }
                    }
                    css {
                        marginBottom = 40.px
                        height = 40.pct
                        display = Display.inlineBlock
                        justifyContent = JustifyContent.spaceBetween

                    }
                }
            }
        }

    }
}

