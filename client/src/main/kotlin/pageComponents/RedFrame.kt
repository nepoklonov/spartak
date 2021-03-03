package pageComponents

import SpartakColors
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
                backgroundColor = SpartakColors.red
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
            styledDiv {

                styledH2 {
                    css {
                        width = 100.pct
                        display = Display.flex
                        justifyContent = JustifyContent.center
                    }
                    if (props.isTrainer) {
                        +"Тренер"
                    } else {
                        +props.teamMember!!.number
                    }
                }
                styledDiv {
                    css {
                        backgroundRepeat = BackgroundRepeat.noRepeat
                        backgroundSize = "contain"
                        backgroundPosition = "center"
                        margin = "auto"

                        if (props.isTrainer) {
                            backgroundImage = Image("url(/images/${props.trainer!!.photo})")
                            height = 390.px
                            width = 310.px
                        } else {
                            backgroundImage = Image("url(/images/teams/${props.teamMember!!.photo})")
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
                        css {
                            width = 100.pct
                            display = Display.flex
                            justifyContent = JustifyContent.center
                            margin(5.px)
                        }
                        +props.teamMember!!.firstName
                        +" "
                        +props.teamMember!!.lastName
                    }
                    styledDiv {
                        css {
                            width = 100.pct
                            display = Display.flex
                            justifyContent = JustifyContent.center
                            margin(5.px)
                        }
                        +props.teamMember!!.birthday
                    }
                    styledDiv {
                        css {
                            width = 100.pct
                            display = Display.flex
                            justifyContent = JustifyContent.center
                            margin(5.px)
                        }
                        +props.teamMember!!.city
                    }
                }
            }
            if (!props.isTrainer) {
                if (props.teamMember!!.teamRole != "") {
                    styledDiv {
                        css {
                            position = Position.relative
                            zIndex = 1
                            bottom = 400.px
                            left = 33.px
                            width = 50.px
                            height = 85.px
                            color = SpartakColors.red
                            fontFamily = "Russo"
                            if (props.teamMember!!.teamRole == "К") {
                                background =
                                        "linear-gradient(rgba(252,232,123,1),rgba(249,215,73,1))"
                            }
                            if (props.teamMember!!.teamRole == "А") {
                                background =
                                        "linear-gradient(rgba(219,219,219,1), rgba(181,181,181,1))"
                            }
                            display = Display.flex
                            justifyContent = JustifyContent.center
                            alignItems = Align.center
                        }
                        +props.teamMember!!.teamRole
                    }
                }
            }

        }
    }
}