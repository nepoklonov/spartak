package structure

import ColorSpartak
import adminPageComponents.AdminButtonType
import adminPageComponents.SmallNavigationForm
import adminPageComponents.adminButton
import kotlinx.css.*
import kotlinx.css.properties.TextDecoration
import model.NavigationDTO
import pages.GalleryProps
import react.*
import react.dom.h2
import react.router.dom.navLink
import styled.css
import styled.styledDiv

class SmallNavigationState : RState {
    var addForm: Boolean = false
    var editForm: NavigationDTO? = null
}

external interface SmallNavigationProps : RProps {
    var navLines: List<NavigationDTO>
    var selectedLineLink: String
    var addFunction: (List<String>) -> Unit
    var editFunction: (Int, List<String>) -> Unit
    var deleteFunction: (Int) -> Unit
}


class SmallNavigation : RComponent<SmallNavigationProps, SmallNavigationState>() {
    init {
        state = SmallNavigationState()
    }

    private fun editUpdateState(editLine: NavigationDTO) {
        setState {
            editForm = editLine
        }
    }

    private fun addUpdateState() {
        setState {
            addForm = true
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            props.navLines.forEach { navLine ->
                styledDiv {
                    navigationLine(navLine, props.selectedLineLink)
                    adminButton(AdminButtonType.Delete) {
                        props.deleteFunction(navLine.id!!)
                    }
                    state.editForm.let {
                        if (it == navLine) {
                            child(SmallNavigationForm::class) {
                                attrs.inputValues = listOf(it.header, it.link)
                                attrs.onSubmitFunction = { listOfInputValues: List<String> ->
                                    props.editFunction(it.id!!, listOfInputValues)
                                }
                            }
                        } else {
                            adminButton(AdminButtonType.Edit) {
                                editUpdateState(navLine)
                            }
                        }
                    }
                }
            }
            if (state.addForm) {
                child(SmallNavigationForm::class) {
                    attrs {
                        inputValues = listOf("", "")
                        onSubmitFunction = props.addFunction
                    }
                }
            } else {
                adminButton(AdminButtonType.Add) {
                    addUpdateState()
                }
            }
        }
    }

    private fun RBuilder.navigationLine(string: NavigationDTO, selectedLine: String) {
        styledDiv {
            css {
                paddingTop = 20.px
                paddingBottom = 20.px
                textAlign = TextAlign.center
                fontSize = 25.px
                if (string.link == selectedLine) {
                    borderLeftColor = ColorSpartak.Red.color
                    borderLeftWidth = 5.px
                    borderLeftStyle = BorderStyle.solid
                }
                child("a") {
                    textDecoration = TextDecoration.none
                    color = Color.black
                    if (string.link == selectedLine) {
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