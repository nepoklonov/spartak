package view

import kotlinx.css.*
import kotlinx.css.properties.LineHeight
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.a
import styled.css
import styled.styledDiv
import styled.styledH1
import styled.styledImg

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
                        fontSize = 36.pt
                        lineHeight = LineHeight.normal
                    }
                    +"Молодежный хоккейный клуб «Спартак»"
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
                    css{
                        display = Display.flex
                        alignItems = Align.center
                        justifyContent = JustifyContent.spaceBetween
                    }
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

