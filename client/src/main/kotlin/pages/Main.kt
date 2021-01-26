package pages

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.styledDiv
import view.ApplicationComponent
import kotlin.coroutines.CoroutineContext

class Main : RComponent<RProps, RState>(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Job()

    override fun RBuilder.render() {
        styledDiv {
            child(ApplicationComponent::class) {
                attrs.coroutineScope = this@Main
            }
        }
    }

}
