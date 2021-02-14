package pageComponents

import kotlinx.css.*
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import styled.css
import styled.styledDiv
import styled.styledH1
import styled.styledImg


class FooterComponent : RComponent<RProps, RState>() {

    private fun RBuilder.addInfo(iconSrc: String, text: String) {
        styledDiv {
            css {
                display = Display.flex
                alignItems = Align.center
            }
            styledImg(src = iconSrc) {
                css {
                    float = Float.left
                    height = 20.px
                    margin = 5.px.toString()
                }
            }
            +text
        }
    }

    private fun RBuilder.addOnPage(it: TextWithIcon) {
        styledDiv {
            css {
                overflow = Overflow.hidden
            }
            styledH1 {
                +it.header
            }
            addInfo(it.iconSrc, it.text)
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                display = Display.flex
                overflow = Overflow.hidden
                padding = 40.px.toString()
                background = "linear-gradient(180deg, rgba(223, 221, 221, 0) 0%, #5E5555 100%)"
            }

            styledDiv {
                css {
                    margin = 20.px.toString()
                }
                addOnPage(TextWithIcon.Phone)
                addOnPage(TextWithIcon.Vk)
                TextWithIcon.Inst.let {
                    addInfo(it.iconSrc, it.text)
                }
            }
            styledDiv {
                css {
                    margin = 20.px.toString()
                    width = 300.px
                }
                addOnPage(TextWithIcon.Mail)
                addOnPage(TextWithIcon.Address)
            }
            styledDiv {
                css {
                    margin = 20.px.toString()
                    width = 400.px
                }
                styledH1 {
                    +"Адрес на карте:"
                }
                styledImg(src = "/images/map.jpg") {}
            }

        }

    }
}