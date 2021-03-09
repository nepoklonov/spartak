package pages.products

import header
import isAdmin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import loading
import model.ProductDTO
import react.*
import services.ProductService
import styled.styledDiv
import styled.styledImg

external interface ProductsProps : RProps {
    var coroutineScope: CoroutineScope
}

class ProductsState : RState {
    var products: List<ProductDTO>? = null
//    var selectedProductIndex: Int? = null
}

class Products : RComponent<ProductsProps, ProductsState>() {
    private val coroutineContext
        get() = props.coroutineScope.coroutineContext

    override fun componentDidMount() {
        val productService = ProductService(coroutineContext)
        props.coroutineScope.launch {
            val listOfProducts = productService.getAllProducts()
            setState {
                products = listOfProducts
            }
        }
    }

    override fun RBuilder.render() {
        header { +"Товары" }
        styledDiv {
            state.products?.let { products ->
                products.forEach { product ->
                    styledDiv {
                        +product.name
                        +product.cost
                        styledImg(src = product.photo) {}
                    }
                    if (isAdmin){
                        child(EditAndDeleteButtons::class){
                            attrs.coroutineScope = props.coroutineScope
                            attrs.product = product
                        }
                    }
                }
            }?: run { loading() }
            if(isAdmin) {
                child(AddForm::class) {
                    attrs.coroutineScope = props.coroutineScope
                }
            }
        }
    }
}