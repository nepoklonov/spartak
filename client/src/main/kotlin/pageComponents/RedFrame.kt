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
                alignItems = Align.center
                if (props.isTrainer) {
                    height = 515.px
                    width = 335.px
                } else {
                    height = 410.px
                    width = 220.px
                }
            }
            styledH2 {
                if (props.isTrainer) {
                    +"Тренер"
                } else {
                    +props.teamMember!!.id.toString()
                }
            }
            styledDiv {
                css {
                    if (props.isTrainer) {
//                        backgroundImage = Image("url = /images/${props.trainer!!.photo}")
                        backgroundImage = Image("url = /images/map.jpg")
                        backgroundSize = 390.px.toString()
                        height = 390.px
                        width = 310.px
                    } else {
//                        backgroundImage = Image("url = /images/${props.teamMember!!.photo}")
                        backgroundImage = Image("url = /images/map.jpg")
                        backgroundSize = 250.px.toString()
                        height = 250.px
                        width = 200.px
                    }
                }
            }
            if (props.isTrainer){
                + props.trainer!!.name
            }else{
                styledDiv {
                    + props.teamMember!!.firstName
                    + props.teamMember!!.lastName
                }
                styledDiv {
                    + props.teamMember!!.birthday
                }
                styledDiv {
                    +props.teamMember!!.city
                }
            }
        }
    }
}