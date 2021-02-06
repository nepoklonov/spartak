package pageComponents

import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import pages.GalleryProps
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
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
                width = 250.px
            }
            styledH2 {
                css {
                    margin = 40.px.toString()
                    child("a") {
                        textDecoration = TextDecoration.none
                        color = Color.black
                        if (props.selectedLink == props.link) {
                            color = ColorSpartak.Red.color
                            borderBottomColor = ColorSpartak.Red.color
                            borderBottomWidth = 3.px
                            borderBottomStyle = BorderStyle.solid
                        }
                    }
                }
                navLink<GalleryProps>(to = props.link) {
                    +props.string
                }
            }
        }
    }
}