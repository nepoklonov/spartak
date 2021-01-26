package view

import kotlinx.css.*
import react.RBuilder
import react.RProps
import react.RState
import react.dom.a
import styled.css
import styled.styledDiv
import styled.styledH1
import styled.styledImg

class HeaderComponent : RootComponent<RProps, RState>() {
    private fun RBuilder.addIcon(src: String, floatDirection: Float) {
        styledImg(src = src) {
            css {
                height = 20.px
                float = floatDirection
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
                padding = 40.px.toString()
                overflow = Overflow.hidden
            }
            styledImg(src = "/images/logo.png") {
                css {
                    height = 200.px
                    float = Float.left
                }
            }

            styledH1 {
                css {
                    fontSize = 32.pt
                    fontFamily = "Russo"
                    width = 700.px
                }
                +"Молодежный хоккейный клуб «Спартак»"
            }

            styledDiv {
                css {
                    float = Float.right
                    width = 300.px
                }

                styledDiv {
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