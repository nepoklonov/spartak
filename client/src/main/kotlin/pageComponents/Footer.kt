package pageComponents

import kotlinx.css.*
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.InnerHTML
import styled.css
import styled.styledDiv
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
            styledDiv {
                css{
                    fontSize = 25.px
                    margin(30.px)
                }
                +it.header
            }
            addInfo(it.iconSrc, it.text)
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                display = Display.flex
                justifyContent = JustifyContent.spaceBetween
                padding = 40.px.toString()
                background = "linear-gradient(180deg, rgba(223, 221, 221, 0) 0%, #5E5555 100%)"
            }

            styledDiv {
                addOnPage(TextWithIcon.Phone)
                addOnPage(TextWithIcon.Vk)
                TextWithIcon.Inst.let {
                    addInfo(it.iconSrc, it.text)
                }
            }
            styledDiv {
                addOnPage(TextWithIcon.Mail)
                addOnPage(TextWithIcon.Address)
            }
            styledDiv {
                css {
                    width = 400.px
                }
                styledDiv {
                    css{
                        fontSize = 25.px
                        margin(30.px)
                    }
                    +"Адрес на карте:"
                }
                styledDiv {
                    attrs["dangerouslySetInnerHTML"] = InnerHTML("<iframe src=\"https://yandex.ru/map-widget/v1/?um=constructor%3A36ba0b6e5870ceca959a551496be2f606620f725e1f212c0ef8aa0abc8af3aff&amp;source=constructor\" width=\"500\" height=\"300\" frameborder=\"0\"></iframe>")
                }
            }

        }

    }
}