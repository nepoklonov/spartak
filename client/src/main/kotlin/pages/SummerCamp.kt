package pages

import adminPageComponents.EditorComponent
import isAdmin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.css.ObjectFit
import kotlinx.css.objectFit
import loading
import react.*
import react.dom.InnerHTML
import services.HtmlService
import styled.css
import styled.styledDiv

external interface SummerCampProps : RProps {
    var coroutineScope: CoroutineScope
}

class SummerCampState : RState {
    var summerCampHtml: String? = null
}

class SummerCamp : RComponent<SummerCampProps, SummerCampState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val htmlService = HtmlService(coroutineContext)

        props.coroutineScope.launch {
            val summerCampHtml = htmlService.getHtml("htmlPages/SummerCamp.html")
            setState{
                this.summerCampHtml = summerCampHtml
            }
        }
    }

    override fun RBuilder.render() {
        if ((isAdmin) && (state.summerCampHtml != null)) {
            child(EditorComponent::class) {
                attrs.text = state.summerCampHtml!!
                attrs.coroutineScope = props.coroutineScope
                attrs.url = "htmlPages/SummerCamp.html"
            }
        }
        styledDiv {
            css {
                child("div"){
                    child("img"){
                        objectFit = ObjectFit.cover
                    }
                }
            }
            state.summerCampHtml?.let {
                attrs["dangerouslySetInnerHTML"] = InnerHTML(state.summerCampHtml!!)
            } ?: run { loading() }
        }
    }
}