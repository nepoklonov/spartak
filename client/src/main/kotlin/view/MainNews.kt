package pages

import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.dom.addClass
import org.w3c.dom.asList
import org.w3c.dom.get
import react.*
import react.dom.InnerHTML
import services.HtmlService
import styled.styledDiv

external interface MainNewsProps : RProps {
    var coroutineScope: CoroutineScope
}

class MainNewsState : RState {
    var error: Throwable? = null
    var newsHtml: String? = null
}

class MainNews : RComponent<MainNewsProps, MainNewsState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext


    override fun componentDidMount() {
        val htmlService = HtmlService(coroutineContext)

        props.coroutineScope.launch {
            val newsHtml = try {
                        htmlService.getHtml("htmlPages/News.html")
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }
            val e = document.createElement("div")
            e.innerHTML = newsHtml
            val news = e.getElementsByClassName("news-item")
            console.log(news.length)
            for (j in 0..news.length) {
                var pics = news[j]?.getElementsByTagName("img")
                if (pics != null) {
                    for (i in 0..pics.length) {
                        if (i > 0) {
                            pics[i]?.remove()
                            console.log(i)
                            console.log(pics[i])
                        }
                    }
                }
                console.log(news[j]?.innerHTML)
                news[j]?.innerHTML = news[j]?.innerHTML?.subSequence(0,400)?.lastIndexOf(".")?.let {
                    news[j]?.innerHTML?.subSequence(0,
                            it)
                }as String +"..."
            }
            setState() {
                this.newsHtml = e.outerHTML
            }
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            if (state.newsHtml != null) {
                attrs["dangerouslySetInnerHTML"] = InnerHTML(state.newsHtml!!)
            } else {
                +"загрузка..."
            }
        }

    }
}

