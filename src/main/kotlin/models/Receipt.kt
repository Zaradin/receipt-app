package models

import java.time.LocalDate

/**
 * A data class that represents a receipt.
 *
 * @property storeName the name of the store where the receipt was issued
 * @property category the category of the receipt
 * @property description the description of the receipt
 * @property dateOfReceipt the date when the receipt was issued
 * @property paymentMethod the payment method used to pay for the receipt
 * @property products a mutable set of products that were bought on this receipt
 *
 * @author Josh Crotty
 * @since 2.0
 */
data class Receipt(
    var storeName: String,
    var category: String,
    var description: String,
    var dateOfReceipt: LocalDate,
    var paymentMethod: String,
    var products: MutableSet<Product> = mutableSetOf()
) {

    private var lastProductID = 0

    /**
     * Returns the ID for a new product.
     */
    private fun getProductID() = lastProductID++

    /**
     * Adds a new [product] to this receipt.
     *
     * @return `true` if the product was successfully added, `false` otherwise
     */
    fun addProduct(product: Product): Boolean {
        product.productID = getProductID()
        return products.add(product)
    }

    /**
     * Returns the number of products in this receipt.
     */
    fun numberOfProducts() = products.size

    /**
     * Finds the product with the given [id].
     *
     * @return the product with the given ID, or `null` if no such product was found
     */
    fun findOne(id: Int): Product? {
        return products.find { product -> product.productID == id }
    }

    /**
     * Deletes the product with the given [id] from this receipt.
     *
     * @return `true` if the product was successfully deleted, `false` otherwise
     */
    fun deleteProduct(id: Int): Boolean {
        return products.removeIf { product -> product.productID == id }
    }

    /**
     * Updates the product with the given [id] to have the properties of the [newProduct].
     *
     * @return `true` if the product was successfully updated, `false` otherwise
     */
    fun updateProduct(id: Int, newProduct: Product): Boolean {
        val productToUpdate = findOne(id)
        if (productToUpdate != null) {
            productToUpdate.productName = newProduct.productName
            productToUpdate.productPrice = newProduct.productPrice
            productToUpdate.quantityBought = newProduct.quantityBought
            return true
        }
        return false
    }

    /**
     * Lists all products on this receipt.
     *
     * @return a string containing a list of all products, or "NO PRODUCTS FOUND" if the receipt is empty
     */
    fun listProducts() =
        if (products.isEmpty()) "\tNO PRODUCTS FOUND"
        else products.mapIndexed { index, product -> "$index: $product" }.joinToString(separator = "\n")

    /**
     * Returns the total amount spent on this receipt.
     */
    fun totalSpendForReceipt(): Double {
        return products.sumOf { it.productPrice * it.quantityBought }
    }
}
