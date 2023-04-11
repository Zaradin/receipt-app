package models

import java.time.LocalDate

data class Receipt (var storeName: String,
                    var category :String,
                    var description :String,
                    var dateOfReceipt : LocalDate,
                    var paymentMethod : String,
                    var products: MutableSet<Product> = mutableSetOf()) {

    private var lastProductID = 0
    private fun getProductID() = lastProductID++

    /*Functions for controlling products
    *
    *
    * */

    // Add new product to receipt
    fun addProduct(product: Product): Boolean {
        product.productID = getProductID()
        return products.add(product)
    }

    // get number of products in receipt
    fun numberOfProducts() = products.size

    // Find product by id
    fun findOne(id: Int): Product? {
        return products.find { product -> product.productID == id }
    }

    // delete product by ID from a receipt
    fun deleteProduct(id: Int): Boolean {
        return products.removeIf { product -> product.productID == id }
    }

    // update the product
//    fun updateProduct(id: Int, newProduct: Product): Boolean {
//        val productToUpdate = findOne(id)
//        if(productToUpdate != null){
//
//        }
//    }

    // list products
    fun listProducts() =
        if (products.isEmpty()) "\tNO PRODUCTS FOUND"
        else products.mapIndexed { index, product ->
            "$index: ${product.toString()}}\n"

        }
}