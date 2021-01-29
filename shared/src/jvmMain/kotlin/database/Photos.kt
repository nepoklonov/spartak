package database

import org.jetbrains.exposed.dao.id.IntIdTable

object Photos: IntIdTable() {
    val url = text("url")
    val gallerySection = text("gallerySection")
}