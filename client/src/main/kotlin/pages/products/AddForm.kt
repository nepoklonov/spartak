package pages.products

import adminPageComponents.AdminButtonType
import adminPageComponents.adminButton
import consts.Input
import consts.productInputs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onSubmitFunction
import model.ProductDTO
import org.w3c.files.File
import pageComponents.FormState
import pageComponents.formComponent
import react.RBuilder
import react.RComponent
import react.RProps
import react.setState
import services.ProductService
import styled.styledForm

external interface AddFormProps : RProps {
    var coroutineScope: CoroutineScope
}

class AddFormState : FormState {
    var addForm: Boolean = false
    override var inputs: MutableMap<String, Input> = productInputs
    override var file: File? = null
}

class AddForm : RComponent<AddFormProps, AddFormState>() {

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    init {
        state.addForm = false
        state.inputs = productInputs
    }

    override fun RBuilder.render() {
        if (!state.addForm) {
            adminButton(AdminButtonType.Add) {
                setState {
                    addForm = true
                }
            }
        } else {
            styledForm {
                attrs.onSubmitFunction = { event ->
                    event.preventDefault()
                    event.stopPropagation()
                    val productService = ProductService(coroutineContext)
                    props.coroutineScope.launch {
                        var formIsCompleted = true
                        state.inputs.values.forEach {
                            if (it.isRed) {
                                formIsCompleted = false
                            }
                        }
                        if (formIsCompleted) {
                            productService.addProduct(
                                ProductDTO(
                                    null,
                                    state.inputs["name"]!!.inputValue,
                                    state.inputs["cost"]!!.inputValue,
                                    "address.png"
                                )
                            )
                        }
                    }
                }
                formComponent(this)
            }
        }
    }
}