package structure

import kotlinx.css.*
import Consts.TextWithIcon
import kotlinext.js.jsObject
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
                padding(0.px, 50.px)
                marginTop = 50.px
                background = "linear-gradient(180deg, rgba(223, 221, 221, 0) -10%, #333333 100%)"
            }

            styledDiv {
                css{
                    margin(15.px)
                }
                addOnPage(TextWithIcon.Phone)
                addOnPage(TextWithIcon.Vk)
                TextWithIcon.Inst.let {
                    addInfo(it.iconSrc, it.text)
                }
            }
            styledDiv {
                css {
                    margin(15.px)
                }
                addOnPage(TextWithIcon.Mail)
                addOnPage(TextWithIcon.Address)
            }

            styledDiv {
                css{
                    media("(max-width: 1430px)"){
                        visibility = Visibility.hidden
                        fontSize = 26.px
                    }
                    margin(15.px)
                }

                styledDiv {
                    css{
                        margin(30.px)
                        fontSize = 25.px
                    }
                    +"Адрес на карте:"
                }
                styledDiv {
                    attrs["dangerouslySetInnerHTML"] = jsObject<InnerHTML> {
                        //language=HTML
                        __html = "<iframe src=\"https://yandex.ru/map-widget/v1/?um=constructor%3A36ba0b6e5870ceca959" +
                            "a551496be2f606620f725e1f212c0ef8aa0abc8af3aff&amp;source=constructor\" width=\"400\" " +
                            "height=\"225\" frameborder=\"0\"></iframe>"
                    }
                }
            }

        }

    }
}