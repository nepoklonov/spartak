package services

import kotlinx.serialization.builtins.serializer
import model.ProductDTO
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class ProductService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)

    actual suspend fun getAllProducts(): List<ProductDTO> {
        return transport.getList("getAllProducts", ProductDTO.serializer())
    }

    actual suspend fun addProduct(product: ProductDTO): Int {
        return transport.post("addProduct", Int.serializer(), "product" to product)
    }

    actual suspend fun editProduct(product: ProductDTO): Boolean {
        return transport.post("editProduct", Boolean.serializer(), "product" to product)
    }

    actual suspend fun deleteProduct(id: Int): Boolean {
        return transport.post("deleteProduct", Boolean.serializer(), "id" to id)
    }
}