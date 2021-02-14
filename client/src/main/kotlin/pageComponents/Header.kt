package pageComponents

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
    private fun RBuilder.addIcon(src: String) {
        styledImg(src = src) {
            css {
                height = 20.px
                margin = 5.px.toString()
            }
        }
    }

    private fun RBuilder.addIconOnPage(src: String, text: String, isLinked: Boolean) {

        if (isLinked) {
            a(href = text) {
                addIcon(src)
            }
        } else {
            addIcon(src)
            +text
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                minHeight = 300.px
                display = Display.flex
                justifyContent = JustifyContent.spaceAround
                alignItems = Align.center
            }

            styledImg(src = "/images/logo.png") {
                css {
                    width = 200.px
                    height = 231.px
                }
            }

            styledH1 {
                css {
                    alignSelf = Align.center
                    fontSize = 60.px
                    lineHeight = LineHeight.normal
                }
                +"Молодежный хоккейный клуб «Спартак»"
            }

            styledDiv {
                css {
                    fontSize = 16.pt
                    alignItems = Align.center
                    width = 450.px
                    marginRight= 30.px
                }

                styledDiv {
                    css {
                        lineHeight = LineHeight.normal
                        marginBottom = 30.px
                    }
                    TextWithIcon.Address.let {
                        addIconOnPage(it.iconSrc, it.text, it.isLinked)
                    }
                }
                styledDiv {
                    css {
                        display = Display.flex
                        alignItems = Align.center
                        justifyContent = JustifyContent.spaceBetween
                    }
                    TextWithIcon.Phone.let {
                        addIconOnPage(it.iconSrc, it.text, it.isLinked)
                    }
                    TextWithIcon.Vk.let {
                        addIconOnPage(it.iconSrc, it.text, it.isLinked)
                    }
                    TextWithIcon.Inst.let {
                        addIconOnPage(it.iconSrc, it.text, it.isLinked)
                    }
                }

            }
        }
    }
}

