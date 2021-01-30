package view

import headerText
import kotlinx.css.*
import kotlinx.css.VerticalAlign.Companion.top
import kotlinx.css.properties.LineHeight
import kotlinx.css.properties.TextDecoration
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.a
import styled.*

class HeaderComponent : RComponent<RProps, RState>() {
    private fun RBuilder.addIcon(src: String, floatDirection: Float) {
        styledImg(src = src) {
            css {
                height = 20.px
                margin = 5.px.toString()
            }
        }
    }

    private fun RBuilder.addIconOnPage(src: String, text: String, isLinked: Boolean, floatDirection: Float) {

        if (isLinked) {
            a(href = text) {
                addIcon(src, floatDirection)
            }
        } else {
            addIcon(src, floatDirection)
            +text
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                height = 230.px
                display = Display.flex
                justifyContent = JustifyContent.spaceEvenly
                alignItems = Align.center
            }
            styledDiv {
                css {
                    padding = 0.px.toString()
                    overflow = Overflow.hidden
                    float = Float.left
                    width = 880.px
                }
                styledImg(src = "/images/logo.png") {
                    css {
                        height = 200.px
                        float = Float.left
                        border = 20.px.toString()
                    }
                }

                styledH1 {
                    css {
                        alignSelf = Align.center
                        fontSize = 18.pt
                        lineHeight = LineHeight.normal
                    }
                    headerText { +"Молодежный хоккейный клуб «Спартак»" }
                }
            }

            styledDiv {
                css {
                    fontSize = 16.pt
                    alignItems = Align.center
                    border = 20.px.toString()
                }

                styledDiv {
                    css{
                        lineHeight = LineHeight.normal
                        width = 320.px
                    }
                    TextWithIcon.Address.let {
                        addIconOnPage(it.iconSrc, it.text, it.isLinked, Float.left)
                    }
                }
                styledDiv {
                    TextWithIcon.Phone.let {
                        addIconOnPage(it.iconSrc, it.text, it.isLinked, Float.left)
                    }
                    TextWithIcon.Vk.let {
                        addIconOnPage(it.iconSrc, it.text, it.isLinked, Float.right)
                    }
                    TextWithIcon.Inst.let {
                        addIconOnPage(it.iconSrc, it.text, it.isLinked, Float.right)
                    }
                }

            }
        }
    }
}

