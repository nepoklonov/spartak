package pages

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import react.*
import react.dom.InnerHTML
import services.HtmlService
import styled.styledDiv

external interface MainProps : RProps {
    var coroutineScope: CoroutineScope
}

class MainState : RState {
    var error: Throwable? = null
    var mainHtml: String? = null
}

class Main : RComponent<MainProps, MainState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val htmlService = HtmlService(coroutineContext)

        props.coroutineScope.launch {
            val mainHtml = try {
                htmlService.getHtml("htmlPages/Main.html")
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }

            setState() {
                this.mainHtml = mainHtml
            }
        }

    }

    override fun RBuilder.render() {
        styledDiv {
            if (state.mainHtml != null) {
                attrs["dangerouslySetInnerHTML"] = InnerHTML(state.mainHtml!!)
            } else {
                +"загрузка..."
            }
        }
//        cutHTMLMain()
    }
}

//fun cutHTMLMain() {
//    var pics = document.getElementsByTagName("img")
//    console.log(pics.length)
//    for (i in 0..pics.length){
//        if (i>0) pics[i]?.outerHTML  = ""
//        console.log(pics[i])
//    }
//}
