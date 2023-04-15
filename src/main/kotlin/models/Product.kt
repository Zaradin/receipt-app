package models

/**
 * Represents a product that can be bought as part of a receipt.
 * @property productID The unique identifier of the product.
 * @property productName The name of the product.
 * @property productPrice The price of the product.
 * @property quantityBought The quantity of the product that was bought.
 * @author Josh Crotty
 * @since 2.0
 */
data class Product(
    var productID: Int = 0,
    var productName: String,
    var productPrice: Double,
    var quantityBought: Int
)
