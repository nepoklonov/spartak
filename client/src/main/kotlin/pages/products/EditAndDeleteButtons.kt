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

external interface EditAndDeleteButtonsProps : RProps {
    var coroutineScope: CoroutineScope
    var product: ProductDTO
}

class EditAndDeleteButtonsState : FormState {
    override var inputs: MutableMap<String, Input> = productInputs
    var editProduct: ProductDTO? = null
    override var file: File? = null
}

class EditAndDeleteButtons : RComponent<EditAndDeleteButtonsProps, EditAndDeleteButtonsState>() {
    init {
        state.inputs = productInputs
        state.editProduct = null
        state.file = null
    }

    private val coroutineContext
        get() = props.coroutineScope.coroutineContext


    override fun RBuilder.render() {
        adminButton(AdminButtonType.Delete) {
            val productService = ProductService(coroutineContext)
            props.coroutineScope.launch {
                productService.deleteProduct(props.product.id!!)
            }
        }
        if (state.editProduct != props.product) {
            adminButton(AdminButtonType.Edit) {
                setState {
                    editProduct = props.product
                    inputs["name"]!!.inputValue = props.product.name
                    inputs["cost"]!!.inputValue = props.product.cost
                    inputs["photo"]!!.inputValue = props.product.photo
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
                            productService.editProduct(
                                ProductDTO(
                                    props.product.id,
                                    state.inputs["name"]!!.inputValue,
                                    state.inputs["cost"]!!.inputValue,
                                    state.inputs["photo"]!!.inputValue,
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