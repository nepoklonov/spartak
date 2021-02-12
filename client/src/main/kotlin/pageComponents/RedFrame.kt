package pageComponents

import kotlinx.css.*
import model.TeamMemberDTO
import model.TrainerDTO
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css
import styled.styledDiv
import styled.styledH2

external interface RedFrameProps : RProps {
    var isTrainer: Boolean
    var trainer: TrainerDTO?
    var teamMember: TeamMemberDTO?
}

class RedFrameComponent : RComponent<RedFrameProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                backgroundColor = ColorSpartak.Red.color
                color = Color.white
                margin(25.px)
                if (props.isTrainer) {
                    height = 515.px
                    width = 335.px
                } else {
                    height = 410.px
                    width = 220.px
                }
            }
            styledH2 {
                css {
                    width = 100.pct
                    display = Display.flex
                    justifyContent = JustifyContent.center
                }
                if (props.isTrainer) {
                    +"Тренер"
                } else {
                    +props.teamMember!!.id.toString()
                }
            }
            styledDiv {
                css {
                    backgroundRepeat = BackgroundRepeat.noRepeat
                    backgroundSize = "contain"
                    backgroundPosition = "center"

                    if (props.isTrainer) {
                        backgroundImage = Image("url(/images/${props.trainer!!.photo})")
                        height = 390.px
                        width = 310.px
                    } else {
                        backgroundImage = Image("url(/images/${props.teamMember!!.photo})")
                        height = 250.px
                        width = 200.px
                    }
                }
            }
            if (props.isTrainer) {
                styledDiv {
                    css {
                        width = 100.pct
                        display = Display.flex
                        justifyContent = JustifyContent.center
                    }
                    +props.trainer!!.name
                }
            } else {
                styledDiv {
                    css{
                        width = 100.pct
                        display = Display.flex
                        justifyContent = JustifyContent.center
                    }
                    +props.teamMember!!.firstName
                    +props.teamMember!!.lastName
                }
                styledDiv {
                    css{
                        width = 100.pct
                        display = Display.flex
                        justifyContent = JustifyContent.center
                    }
                    +props.teamMember!!.birthday
                }
                styledDiv {
                    css{
                        width = 100.pct
                        display = Display.flex
                        justifyContent = JustifyContent.center
                    }
                    +props.teamMember!!.city
                }
            }
        }
    }
}