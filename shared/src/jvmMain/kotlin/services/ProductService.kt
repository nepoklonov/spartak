package services

import database.Products
import database.database
import model.ProductDTO
import org.jetbrains.exposed.sql.*
import rpc.RPCService

actual class ProductService : RPCService {
    actual suspend fun getAllProducts(): List<ProductDTO> {
        val listOfProducts = mutableListOf<ProductDTO>()
        database {
            Products.selectAll().orderBy(Products.id to SortOrder.ASC).forEach {
                listOfProducts += ProductDTO(
                    it[Products.id].value,
                    it[Products.name],
                    it[Products.cost],
                    it[Products.photo]
                )
            }
        }
        return listOfProducts
    }

    //    @RequireRole(Role.Admin)
    actual suspend fun addProduct(product: ProductDTO): Int {
        return database {
            Products.insertAndGetId {
                it[name] = product.name
                it[cost] = product.cost
                it[photo] = product.photo
            }
        }.value
    }

    //    @RequireRole(Role.Admin)
    actual suspend fun editProduct(product: ProductDTO): Boolean {
        database {
            Products.update({ Products.id eq product.id }) {
                it[name] = product.name
                it[cost] = product.cost
                it[photo] = product.photo
            }
        }
        return true
    }

    //    @RequireRole(Role.Admin)
    actual suspend fun deleteProduct(id: Int): Boolean {
        database {
            Products.deleteWhere { Products.id eq id }
        }
        return true
    }
}