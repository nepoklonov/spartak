package services

import model.ProductDTO

expect class ProductService {
    suspend fun getAllProducts(): List<ProductDTO>
    suspend fun addProduct(product: ProductDTO): Int
    suspend fun editProduct(product: ProductDTO): Boolean
    suspend fun deleteProduct(id: Int): Boolean
}