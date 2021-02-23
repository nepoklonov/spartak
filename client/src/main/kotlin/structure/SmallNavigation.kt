package structure

import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import ColorSpartak
import pages.GalleryProps
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.h2
import react.router.dom.navLink
import styled.css
import styled.styledDiv
import styled.styledH2

external interface SmallNavigationProps : RProps {
    var string: String
    var link: String
    var selectedLink: String
}

class SmallNavigation : RComponent<SmallNavigationProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                fontFamily = "Russo"
                textAlign = TextAlign.center
                width = 325.px
            }
            styledH2 {
                css {
                    paddingTop = 20.px
                    paddingBottom = 20.px
                    fontSize = 25.px
                    if (props.selectedLink == props.link) {
                        borderLeftColor = ColorSpartak.Red.color
                        borderLeftWidth = 5.px
                        borderLeftStyle = BorderStyle.solid
                    }
                    child("a") {
                        textDecoration = TextDecoration.none
                        color = Color.black
                        if (props.selectedLink == props.link) {
                            color = ColorSpartak.Red.color

                        }
                    }
                }
                navLink<GalleryProps>(to = props.link) {
                    h2 {
                        +props.string
                    }
                }
            }
        }
    }
}