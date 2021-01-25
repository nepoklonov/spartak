package view

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import model.Check
import react.*
import services.CheckService
import styled.styledDiv

external interface ApplicationProps : RProps {
    var coroutineScope: CoroutineScope
}

class ApplicationState : RState {
    var error: Throwable? = null
    var check: Check? = null

}

class ApplicationComponent : RComponent<ApplicationProps, ApplicationState>() {
    init {
        state = ApplicationState()
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val checkService = CheckService(coroutineContext)
        props.coroutineScope.launch {
            val check = try {
                checkService.getCheck()
            } catch (e: Throwable) {
                setState {
                    error = e
                }
                return@launch
            }

            setState {
                this.check = check
            }
        }
    }

    override fun RBuilder.render() {
        val error = state.error
        if (error != null) {
            throw error
        }
        styledDiv {
            +(state.check?.checkText ?: "Let's wait.")
        }
    }
}





