package pages

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.styledH1

class Workouts : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        styledH1 {
            +"Тренировки"
        }
    }
}