package pages

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import loading
import react.*
import react.dom.InnerHTML
import services.HtmlService
import styled.styledDiv

external interface AboutProps : RProps {
    var coroutineScope: CoroutineScope
}

class AboutState : RState {
    var aboutHtml: String? = null
}

class About: RComponent<AboutProps, AboutState>() {
    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val htmlService = HtmlService(coroutineContext)

        props.coroutineScope.launch {
            val summerCampHtml = htmlService.getHtml("htmlPages/About.html")
            setState{
                this.aboutHtml = summerCampHtml
            }
        }
    }
    override fun RBuilder.render() {
        styledDiv {
            state.aboutHtml?.let {
                attrs["dangerouslySetInnerHTML"] = InnerHTML(state.aboutHtml!!)
            } ?: run { loading() }
        }
    }
}