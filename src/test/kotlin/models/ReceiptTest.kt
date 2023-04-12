package models

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ReceiptTest {

    private lateinit var receipt: Receipt

    @BeforeEach
    fun setUp() {
        receipt = Receipt(
            storeName = "Dunnes",
            category = "Groceries",
            description = "Some description",
            dateOfReceipt = LocalDate.now(),
            paymentMethod = "Cash",
        )
    }

    @Nested
    inner class AddProducts{
        @Test
        fun `test addProduct adds a product to receipt`() {
            val product = Product(
                productName = "Milk",
                productPrice = 1.99,
                quantityBought = 1,
            )
            assertTrue(receipt.addProduct(product))
            assertEquals(1, receipt.numberOfProducts())
        }

        @Test
        fun `test addProduct sets the productID of the added product`() {
            val product = Product(
                productName = "Milk",
                productPrice = 1.99,
                quantityBought = 1,
            )
            receipt.addProduct(product)
            assertEquals(0, product.productID)
        }
    }

    @Nested
    inner class listProducts{
        @Test
        fun `test numberOfProducts returns the number of products in the receipt`() {
            assertEquals(0, receipt.numberOfProducts())

            val product1 = Product(
                productName = "Milk",
                productPrice = 1.99,
                quantityBought = 1,
            )
            val product2 = Product(
                productName = "Bread",
                productPrice = 2.99,
                quantityBought = 2,
            )
            receipt.addProduct(product1)
            receipt.addProduct(product2)

            assertEquals(2, receipt.numberOfProducts())
        }

        @Test
        fun `test findOne returns the product with the specified ID`() {
            val product1 = Product(
                productName = "Milk",
                productPrice = 1.99,
                quantityBought = 1,
            )
            val product2 = Product(
                productName = "Bread",
                productPrice = 2.99,
                quantityBought = 2,
            )
            receipt.addProduct(product1)
            receipt.addProduct(product2)

            assertEquals(product1, receipt.findOne(product1.productID))
            assertEquals(product2, receipt.findOne(product2.productID))
        }

        @Test
        fun `test findOne returns null if no product is found with the specified ID`() {
            assertNull(receipt.findOne(123))
        }

        @Test
        fun `test listProducts returns a list of products in the receipt`() {
            val product1 = Product(
                productName = "Milk",
                productPrice = 1.99,
                quantityBought = 1,
            )
            val product2 = Product(
                productID = 1,
                productName = "Bread",
                productPrice = 2.99,
                quantityBought = 2,
            )
            val receipt = Receipt(
                storeName = "Aldi",
                category = "Groceries",
                description = "Weekly Groceries",
                dateOfReceipt = LocalDate.of(2022, 4, 1),
                paymentMethod = "Cash",
                products = mutableSetOf(product1, product2)
            )

            val expectedList =
                "0: Product(productID=0, productName=Milk, productPrice=1.99, quantityBought=1)\n1: Product(productID=1, productName=Bread, productPrice=2.99, quantityBought=2)"

            assertEquals(expectedList, receipt.listProducts())
        }
    }

    @Nested
    inner class ProductCRUD{
        @Test
        fun `test deleteProduct removes the product with the specified ID`() {
            val product1 = Product(
                productName = "Milk",
                productPrice = 1.99,
                quantityBought = 1,
            )
            val product2 = Product(
                productName = "Bread",
                productPrice = 2.99,
                quantityBought = 2,
            )
            receipt.addProduct(product1)
            receipt.addProduct(product2)

            assertTrue(receipt.deleteProduct(product1.productID))
            assertEquals(1, receipt.numberOfProducts())
            assertFalse(receipt.deleteProduct(product1.productID))
            assertEquals(1, receipt.numberOfProducts())
        }

        @Test
        fun `test updateProduct updates the product with the specified ID`() {
            val product1 = Product(
                productName = "Milk",
                productPrice = 1.99,
                quantityBought = 1,
            )
            val product2 = Product(
                productName = "Bread",
                productPrice = 2.99,
                quantityBought = 2,
            )
            val receipt = Receipt(
                storeName = "Dunnes",
                category = "Groceries",
                description = "Weekly Groceries",
                dateOfReceipt = LocalDate.of(2022, 4, 1),
                paymentMethod = "Cash",
                products = mutableSetOf(product1, product2)
            )

            val updatedProduct = Product(
                productName = "Yogurt",
                productPrice = 3.49,
                quantityBought = 1
            )

            val updated = receipt.updateProduct(product1.productID, updatedProduct)

            assertTrue(updated)

            val expectedProduct = Product(
                productID = 0,
                productName = "Yogurt",
                productPrice = 3.49,
                quantityBought = 1
            )

            assertEquals(expectedProduct, receipt.findOne(product1.productID))
        }
    }


}