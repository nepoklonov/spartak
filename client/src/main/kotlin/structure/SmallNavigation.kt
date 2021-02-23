package structure

import ColorSpartak
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import model.NavigationDTO
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
    var strings: List<NavigationDTO>
    var selectedLink: String
}

class SmallNavigation : RComponent<SmallNavigationProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            props.strings.forEach { string ->
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
                            if (string.link == props.selectedLink) {
                                borderLeftColor = ColorSpartak.Red.color
                                borderLeftWidth = 5.px
                                borderLeftStyle = BorderStyle.solid
                            }
                            child("a") {
                                textDecoration = TextDecoration.none
                                color = Color.black
                                if (string.link == props.selectedLink) {
                                    color = ColorSpartak.Red.color

                                }
                            }
                        }
                        navLink<GalleryProps>(to = string.link) {
                            h2 {
                                +string.header
                            }
                        }
                    }
                }
            }
        }
    }
}