package pageComponents

import adminPageComponents.AdminButtonComponent
import adminPageComponents.AdminButtonType
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import model.NewsDTO
import org.w3c.dom.asList
import org.w3c.dom.get
import pages.ShortNews
import react.*
import services.HtmlService
import services.NewsService
import styled.*
import kotlin.js.Date

//TODO: зачем оно здесь?
inline fun <T> MutableList<T>.mapInPlace(mutator: (T) -> T) {
    val iterate = this.listIterator()
    while (iterate.hasNext()) {
        val oldValue = iterate.next()
        val newValue = mutator(oldValue)
        if (newValue !== oldValue) {
            iterate.set(newValue)
        }
    }
}


external interface FeedProps : RProps {
    var coroutineScope: CoroutineScope
}

class FeedState : RState {
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
            val newsHtml: MutableList<NewsDTO> = mutableListOf()
            try {
                newsService.getLastNews().forEach { newsHtml.add(NewsDTO(it.id, htmlService.getHtml(it.url), it.date)) }
            } catch (e: Throwable) {
                //TODO: либо не ловить здесь ошибку,
                // либо сделать это чем-то более осмысленным
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
                val p = e.getElementsByTagName("p").asList().map { it.innerHTML }.joinToString(separator = "\n ")
                setState {
                    news.add(ShortNews(h3?.innerHTML, img?.getAttribute("src"), p.substring(0, 400), new.id, new.date))
                }
            }
        }
    }

    override fun RBuilder.render() {
//        if (document.cookie.contains("role=admin")) {
//            styledForm {
//                child(AdminButtonComponent::class) {
//                    attrs.type = "add"
//                }
//                attrs.onSubmitFunction = { event ->
//                    val newsService = NewsService(coroutineContext)
//                    props.coroutineScope.launch {
//                        val id = newsService.getNextNewId()
//                        child(CKEditorComponent::class) {
//                            attrs.text = "Новая новость"
//                            attrs.url = "/newsHtml/${id}.html"
//                            attrs.coroutineScope = props.coroutineScope
//                        }
//                    }
//                }
//            }
//        }
        styledDiv {
            state.news.forEachIndexed { i, it ->

                styledDiv {
                    styledDiv {
                        css {
                            backgroundImage = Image("url('${it.imageSrc}')")
                            backgroundSize = "cover"
                            if (i % 2 == 0) float = Float.left
                            else float = Float.right
                            height = 90.pct
                            width = 40.pct
                            display = Display.flex
                            margin = 1.pct.toString()
                        }
                    }
                    styledDiv {
                        styledDiv {
                            styledH3 {
                                +it.header!!
                            }
                            if (document.cookie.contains("role=admin")) {
                                child(AdminButtonComponent::class) {
                                    attrs.updateState = {
                                        val newsService = NewsService(coroutineContext)
                                        props.coroutineScope.launch {
                                            newsService.deleteNews(it.id!!)
                                        }
                                    }
                                    attrs.button = AdminButtonType.Delete
                                }
                                styledA(href = "/news/${it.id}") {
                                    child(AdminButtonComponent::class) {
                                        attrs.button = AdminButtonType.Edit
                                    }
                                }
                            }
                            styledH5 {
                                +Date(it.date).getDate().toString()
                                +"."
                                +(Date(it.date).getMonth() + 1).toString()
                                +"."
                                +Date(it.date).getFullYear().toString()
                            }
                            +it.content!!.let { it.substring(0, it.length - 40) }
                            it.content.let { it.substring(it.length - 40, it.length) }.forEachIndexed { index, c ->
                                styledSpan {
                                    +c.toString()
                                    css {
                                        opacity = 1 - 0.025 * index
                                    }
                                }
                            }
                        }

                        css {
                            if (i % 2 == 0) float = Float.right
                            else float = Float.left
                            width = 50.pct
                            height = 450.px
                            margin = 1.pct.toString()
                        }
                        styledDiv {
                            styledA(href = "/news/${it.id}") {
                                buttonSecondary("Читать далее")
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

