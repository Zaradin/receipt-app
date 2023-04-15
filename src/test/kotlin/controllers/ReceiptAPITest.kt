package controllers

import models.Product
import models.Receipt
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ReceiptAPITest {

    private var groceryReceipt: Receipt? = null
    private var clothingReceipt: Receipt? = null
    private var electronicsReceipt: Receipt? = null
    private var populatedReceipts: ReceiptAPI? = ReceiptAPI(XMLSerializer(File("receipts.xml")))
    private var emptyReceipts: ReceiptAPI? = ReceiptAPI(XMLSerializer(File("receipts.xml")))

    @BeforeEach
    fun setup() {
        groceryReceipt = Receipt("Dunnes", "Grocery", "Weekly groceries", LocalDate.now(), "Credit Card")
        clothingReceipt = Receipt("TK Maxx", "Clothing", "Summer clothes", LocalDate.of(2022, 6, 15), "Debit Card")
        electronicsReceipt = Receipt("Currys", "Electronics", "New laptop", LocalDate.of(2022, 3, 10), "Cash")

        // adding 3 Receipts to the receipts api
        populatedReceipts!!.add(groceryReceipt!!)
        populatedReceipts!!.add(clothingReceipt!!)
        populatedReceipts!!.add(electronicsReceipt!!)
    }

    @AfterEach
    fun tearDown() {
        groceryReceipt = null
        clothingReceipt = null
        electronicsReceipt = null
        populatedReceipts = null
        emptyReceipts = null
    }

    @Nested
    inner class AddReceipts {
        @Test
        fun `adding a receipt to a populated list adds to ArrayList`() {
            val newReceipt = Receipt("Dunnes", "Grocery", "Grocery Shopping", LocalDate.now(), "Credit")
            assertEquals(3, populatedReceipts!!.numberOfReceipts())
            assertTrue(populatedReceipts!!.add(newReceipt))
            assertEquals(4, populatedReceipts!!.numberOfReceipts())
            assertEquals(newReceipt, populatedReceipts!!.findReceipt(populatedReceipts!!.numberOfReceipts() - 1))
        }

        @Test
        fun `adding a receipt to an empty list adds to ArrayList`() {
            val newReceipt = Receipt("Amazon", "Electronics", "New Laptop", LocalDate.now(), "Debit")
            assertEquals(0, emptyReceipts!!.numberOfReceipts())
            assertTrue(emptyReceipts!!.add(newReceipt))
            assertEquals(1, emptyReceipts!!.numberOfReceipts())
            assertEquals(newReceipt, emptyReceipts!!.findReceipt(emptyReceipts!!.numberOfReceipts() - 1))
        }
    }

    @Nested
    inner class ListReceipts {
        @Test
        fun `listAllReceipts returns No Receipts Stored message when ArrayList is empty`() {
            assertEquals(0, emptyReceipts!!.numberOfReceipts())
            assertTrue(emptyReceipts!!.listAllReceipts().lowercase().contains("no receipts"))
        }

        @Test
        fun `listAllReceipts returns Receipts when ArrayList has receipts stored`() {
            assertEquals(3, populatedReceipts!!.numberOfReceipts())
            val receiptsString = populatedReceipts!!.listAllReceipts().lowercase()
            assertTrue(receiptsString.contains("dunnes"))
            assertTrue(receiptsString.contains("tk maxx"))
            assertTrue(receiptsString.contains("currys"))
            assertTrue(receiptsString.contains("grocery"))
            assertTrue(receiptsString.contains("clothing"))
            assertTrue(receiptsString.contains("electronics"))
        }

        @Test
        fun `test SearchReceipts with a search term`() {
            val searchTerm = "Dunnes"
            val expectedOutput = "0: Receipt(storeName=Dunnes, category=Grocery, description=Weekly groceries, dateOfReceipt=2023-04-15, paymentMethod=Credit Card, products=[])"
            assertEquals(expectedOutput, populatedReceipts!!.searchReceipts(searchTerm))
        }
    }

    @Nested
    inner class CRUDReceipts {

        @Test
        fun `deleteReceipt removes the specified receipt from the ArrayList`() {
            val receiptToDelete = populatedReceipts!!.findReceipt(1)
            assertEquals(3, populatedReceipts!!.numberOfReceipts())
            assertTrue(populatedReceipts!!.deleteReceipt(receiptToDelete!!))
            assertEquals(2, populatedReceipts!!.numberOfReceipts())
        }

        @Test
        fun `deleteReceipt returns false if the specified receipt is not in the ArrayList`() {
            val nonExistentReceipt = Receipt("TestStore", "TestCategory", "TestDescription", LocalDate.now(), "TestPayment")
            assertFalse(populatedReceipts!!.deleteReceipt(nonExistentReceipt))
        }

        @Test
        fun `updateReceipt updates the specified receipt in the ArrayList`() {
            val idToUpdate = 1
            val updatedReceipt = Receipt("UpdatedStore", "UpdatedCategory", "UpdatedDescription", LocalDate.now(), "UpdatedPayment")
            assertTrue(populatedReceipts!!.updateReceipt(idToUpdate, updatedReceipt))
            assertEquals(updatedReceipt.storeName, populatedReceipts!!.findReceipt(idToUpdate)?.storeName)
            assertEquals(updatedReceipt.category, populatedReceipts!!.findReceipt(idToUpdate)?.category)
            assertEquals(updatedReceipt.description, populatedReceipts!!.findReceipt(idToUpdate)?.description)
            assertEquals(updatedReceipt.dateOfReceipt, populatedReceipts!!.findReceipt(idToUpdate)?.dateOfReceipt)
            assertEquals(updatedReceipt.paymentMethod, populatedReceipts!!.findReceipt(idToUpdate)?.paymentMethod)
        }

        @Test
        fun `updateReceipt returns false if the specified receipt is not in the ArrayList`() {
            val idToUpdate = 10
            val updatedReceipt = Receipt("UpdatedStore", "UpdatedCategory", "UpdatedDescription", LocalDate.now(), "UpdatedPayment")
            assertFalse(populatedReceipts!!.updateReceipt(idToUpdate, updatedReceipt))
        }
    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty receipts.XML file.
            val storingReceipts = ReceiptAPI(XMLSerializer(File("receipts.xml")))
            storingReceipts.store()

            // Loading the empty receipts.xml file into a new object
            val loadedReceipts = ReceiptAPI(XMLSerializer(File("receipts.xml")))
            loadedReceipts.load()

            // Comparing the source of the receipts (storingReceipts) with the XML loaded notes (loadedReceipts)
            assertEquals(0, storingReceipts.numberOfReceipts())
            assertEquals(0, loadedReceipts.numberOfReceipts())
            assertEquals(storingReceipts.numberOfReceipts(), loadedReceipts.numberOfReceipts())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 receipts to the receipts.XML file.
            val storingReceipts = ReceiptAPI(XMLSerializer(File("receipts.xml")))
            storingReceipts.add(groceryReceipt!!)
            storingReceipts.add(clothingReceipt!!)
            storingReceipts.add(electronicsReceipt!!)
            storingReceipts.store()

            // Loading receipts.xml into a different collection
            val loadedReceipts = ReceiptAPI(XMLSerializer(File("receipts.xml")))
            loadedReceipts.load()

            // Comparing the source of the receipts (storingReceipts) with the XML loaded notes (loadedReceipts)
            assertEquals(3, storingReceipts.numberOfReceipts())
            assertEquals(3, loadedReceipts.numberOfReceipts())
            assertEquals(storingReceipts.numberOfReceipts(), loadedReceipts.numberOfReceipts())
            assertEquals(storingReceipts.findReceipt(0), loadedReceipts.findReceipt(0))
            assertEquals(storingReceipts.findReceipt(1), loadedReceipts.findReceipt(1))
            assertEquals(storingReceipts.findReceipt(2), loadedReceipts.findReceipt(2))
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty receipts.json file.
            val storingReceipts = ReceiptAPI(JSONSerializer(File("receipts.json")))
            storingReceipts.store()

            // Loading the empty receipts.json file into a new object
            val loadedReceipts = ReceiptAPI(JSONSerializer(File("receipts.json")))
            loadedReceipts.load()

            // Comparing the source of the receipts (storingReceipts) with the JSON loaded notes (loadedReceipts)
            assertEquals(0, storingReceipts.numberOfReceipts())
            assertEquals(0, loadedReceipts.numberOfReceipts())
            assertEquals(storingReceipts.numberOfReceipts(), loadedReceipts.numberOfReceipts())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 receipts to the receipts.json file.
            val storingReceipts = ReceiptAPI(JSONSerializer(File("receipts.json")))
            storingReceipts.add(groceryReceipt!!)
            storingReceipts.add(clothingReceipt!!)
            storingReceipts.add(electronicsReceipt!!)
            storingReceipts.store()

            // Loading receipts.json into a different collection
            val loadedReceipts = ReceiptAPI(JSONSerializer(File("receipts.json")))
            loadedReceipts.load()

            // Comparing the source of the receipts (storingReceipts) with the JSON loaded notes (loadedReceipts)
            assertEquals(3, storingReceipts.numberOfReceipts())
            assertEquals(3, loadedReceipts.numberOfReceipts())
            assertEquals(storingReceipts.numberOfReceipts(), loadedReceipts.numberOfReceipts())
            assertEquals(storingReceipts.findReceipt(0), loadedReceipts.findReceipt(0))
            assertEquals(storingReceipts.findReceipt(1), loadedReceipts.findReceipt(1))
            assertEquals(storingReceipts.findReceipt(2), loadedReceipts.findReceipt(2))
        }
    }

    @Nested
    inner class SpendingAnalysis {
        @Test
        fun `test totalSpendForAllReceipts with empty receipts`() {
            assertEquals(0.0, emptyReceipts!!.totalSpendForAllReceipts())
        }

        @Test
        fun `test totalSpendForAllReceipts with non-empty receipts`() {
            val expectedTotalSpend = groceryReceipt!!.totalSpendForReceipt() +
                clothingReceipt!!.totalSpendForReceipt() +
                electronicsReceipt!!.totalSpendForReceipt()
            assertEquals(expectedTotalSpend, populatedReceipts!!.totalSpendForAllReceipts())
        }

        @Test
        fun `test totalSpendForAllReceipts with one receipt`() {
            emptyReceipts!!.add(groceryReceipt!!)
            assertEquals(groceryReceipt!!.totalSpendForReceipt(), emptyReceipts!!.totalSpendForAllReceipts())
        }

        @Test
        fun `test totalSpendForAllReceipts with multiple receipts`() {
            val receipt1 = Receipt("Store 1", "Category 1", "Description 1", LocalDate.now(), "Credit Card")
            val receipt2 = Receipt("Store 2", "Category 2", "Description 2", LocalDate.now(), "Debit Card")
            val receipt3 = Receipt("Store 3", "Category 3", "Description 3", LocalDate.now(), "Cash")
            emptyReceipts!!.add(receipt1)
            emptyReceipts!!.add(receipt2)
            emptyReceipts!!.add(receipt3)
            val expectedTotalSpend = receipt1.totalSpendForReceipt() + receipt2.totalSpendForReceipt() + receipt3.totalSpendForReceipt()
            assertEquals(expectedTotalSpend, emptyReceipts!!.totalSpendForAllReceipts())
        }

        // averageReceiptSpend
        @Test
        fun `test averageReceiptSpend with one receipt`() {
            val receipt = Receipt("Tesco", "Groceries", "Weekly shopping", LocalDate.now(), "Debit Card")
            emptyReceipts?.add(receipt)
            assertEquals(receipt.totalSpendForReceipt(), emptyReceipts?.averageReceiptSpend())
        }

        @Test
        fun `test averageReceiptSpend with multiple receipts`() {
            val receipt1 = Receipt("Tesco", "Groceries", "Weekly shopping", LocalDate.now(), "Debit Card")
            val receipt2 = Receipt("Dunnes", "Groceries", "Weekly shopping", LocalDate.now(), "Credit Card")
            val receipt3 = Receipt("SuperValu", "Groceries", "Weekly shopping", LocalDate.now(), "Cash")
            emptyReceipts?.add(receipt1)
            emptyReceipts?.add(receipt2)
            emptyReceipts?.add(receipt3)

            val expectedAverage = (receipt1.totalSpendForReceipt() + receipt2.totalSpendForReceipt() + receipt3.totalSpendForReceipt()) / 3.0
            assertEquals(expectedAverage, emptyReceipts?.averageReceiptSpend())
        }

        @Test
        fun `test averageReceiptSpend with receipts from different weeks`() {
            val receipt1 = Receipt("Tesco", "Groceries", "Weekly shopping", LocalDate.now(), "Debit Card")
            val receipt2 = Receipt("Dunnes", "Groceries", "Weekly shopping", LocalDate.now().minusWeeks(1), "Credit Card")
            val receipt3 = Receipt("SuperValu", "Groceries", "Weekly shopping", LocalDate.now().minusWeeks(2), "Cash")
            emptyReceipts?.add(receipt1)
            emptyReceipts?.add(receipt2)
            emptyReceipts?.add(receipt3)

            val expectedAverage = (receipt1.totalSpendForReceipt() + receipt2.totalSpendForReceipt() + receipt3.totalSpendForReceipt()) / 3.0
            assertEquals(expectedAverage, emptyReceipts?.averageReceiptSpend())
        }

        @Test
        fun `test TopCategoriesBySpend with receipts already in arraylist and new ones entered`() {

            // Add more receipts to test top categories calculation
            val sportsReceipt = Receipt("Sports World", "Sports", "New running shoes", LocalDate.of(2022, 3, 3), "Credit Card")
            sportsReceipt.addProduct(Product(productName = "Nike Running Shoes", quantityBought = 2, productPrice = 100.0))
            val bookReceipt = Receipt("Eason", "Books", "New novel", LocalDate.of(2022, 2, 20), "Credit Card")
            bookReceipt.addProduct(Product(productName = "Dune", quantityBought = 1, productPrice = 20.0))
            bookReceipt.addProduct(Product(productName = "The Alchemist", quantityBought = 1, productPrice = 10.0))
            populatedReceipts!!.add(sportsReceipt)
            populatedReceipts!!.add(bookReceipt)

            val expected3 = "sports : €200.00\nbooks : €30.00\ngrocery : €0.00\nclothing : €0.00\nelectronics : €0.00\n"
            val result3 = populatedReceipts!!.topCategoriesBySpend()
            assertEquals(expected3, result3)
        }

        @Test
        fun `test PaymentBreakdown with One Receipt`() {
            val expectedOutput = "Credit Card: 33.33%\nDebit Card: 33.33%\nCash: 33.33%"
            val actualOutput = populatedReceipts!!.paymentBreakdown()
            assertEquals(expectedOutput, actualOutput)
        }

        @Test
        fun `test PaymentBreakdown With Empty Receipts`() {
            val expectedOutput = ""
            val actualOutput = emptyReceipts!!.paymentBreakdown()
            assertEquals(expectedOutput, actualOutput)
        }
    }
}
