package pages

import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import model.NewsDTO
import org.w3c.dom.get
import react.*
import services.HtmlService
import services.NewsService
import styled.*
import kotlin.js.Date

external interface MainNewsProps : RProps {
    var coroutineScope: CoroutineScope
}

data class ShortNews(
    val header: String?,
    val imageSrc: String?,
    val content: String?,
    val id: Int?,
    val date: Long
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
                val newsHtml : MutableList<NewsDTO> = mutableListOf()
                try {
                    newsService.getLastNews(4).forEach { newsHtml.add(NewsDTO(it.id,htmlService.getHtml(it.url), it.date)) }
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
                val p = e.getElementsByTagName("p")[0]
                setState {
                    news.add(ShortNews(h3?.innerHTML, img?.getAttribute("src"), p?.innerHTML?.substring(0, 200), new.id, new.date ))
                }
            }
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            state.news.forEachIndexed { index, it ->
                styledA (href = "/news/${it.id}"){
                    console.log(index)
                    css{
                        textDecoration = TextDecoration.none
                        width = 20.pct
                    }
                    styledDiv {
                        css {
                            backgroundImage = Image("url('${it.imageSrc}')")
                            backgroundSize = "cover"
                            height = 600.px
                            position = Position.relative
                        }
                        styledDiv {
                            css {
                                height = 100.pct
                                background = "linear-gradient(180deg, rgba(255, 255, 255, 0) 0%, rgba(0, 0, 0, 0.71) 41.67%)"
                                hover {
                                    background = "linear-gradient(180deg, rgba(255, 255, 255, 0) 0%, rgba(0, 0, 0, 0.71) 53.67%)"
                                }
                            }
                            styledDiv {

                                    styledH3 {
                                        css {
                                            alignSelf = Align.center
                                        }
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
                                        +it.content!!.let { it.substring(0, it.length-40) }
                                        it.content!!.let { it.substring(it.length-40, it.length) }.forEachIndexed { index, c ->
                                            styledSpan {
                                                +c.toString()
                                                css {
                                                    opacity = 1 - 0.025 * index
                                                }
                                            }
                                        }

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
            }
            css{
                display = Display.flex
                justifyContent = JustifyContent.spaceBetween
                margin = 20.px.toString()
            }
        }

    }
}

