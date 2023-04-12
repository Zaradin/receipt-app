package controllers

import models.Receipt
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
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
    fun setup(){
        groceryReceipt = Receipt("Dunnes", "Grocery", "Weekly groceries", LocalDate.now(), "Credit Card")
        clothingReceipt = Receipt("TK Maxx", "Clothing", "Summer clothes", LocalDate.of(2022, 6, 15), "Debit Card")
        electronicsReceipt = Receipt("Currys", "Electronics", "New laptop", LocalDate.of(2022, 3, 10), "Cash")

        //adding 3 Receipts to the receipts api
        populatedReceipts!!.add(groceryReceipt!!)
        populatedReceipts!!.add(clothingReceipt!!)
        populatedReceipts!!.add(electronicsReceipt!!)
    }

    @AfterEach
    fun tearDown(){
        groceryReceipt = null
        clothingReceipt = null
        electronicsReceipt = null
        populatedReceipts = null
        emptyReceipts = null
    }

    @Nested
    inner class AddReceipts{
        @Test
        fun `adding a receipt to a populated list adds to ArrayList`(){
            val newReceipt = Receipt("Dunnes", "Grocery", "Grocery Shopping", LocalDate.now(), "Credit")
            assertEquals(3, populatedReceipts!!.numberOfReceipts())
            assertTrue(populatedReceipts!!.add(newReceipt))
            assertEquals(4, populatedReceipts!!.numberOfReceipts())
            assertEquals(newReceipt, populatedReceipts!!.findReceipt(populatedReceipts!!.numberOfReceipts() - 1))
        }

        @Test
        fun `adding a receipt to an empty list adds to ArrayList`(){
            val newReceipt = Receipt("Amazon", "Electronics", "New Laptop", LocalDate.now(), "Debit")
            assertEquals(0, emptyReceipts!!.numberOfReceipts())
            assertTrue(emptyReceipts!!.add(newReceipt))
            assertEquals(1, emptyReceipts!!.numberOfReceipts())
            assertEquals(newReceipt, emptyReceipts!!.findReceipt(emptyReceipts!!.numberOfReceipts() - 1))
        }
    }

    @Nested
    inner class ListReceipts{
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

    }

    @Nested
    inner class CRUDReceipts {

        @Test
        fun `deleteReceipt removes the specified receipt from the ArrayList`(){
            val receiptToDelete = populatedReceipts!!.findReceipt(1)
            assertEquals(3, populatedReceipts!!.numberOfReceipts())
            assertTrue(populatedReceipts!!.deleteReceipt(receiptToDelete!!))
            assertEquals(2, populatedReceipts!!.numberOfReceipts())
        }

        @Test
        fun `deleteReceipt returns false if the specified receipt is not in the ArrayList`(){
            val nonExistentReceipt = Receipt("TestStore", "TestCategory", "TestDescription", LocalDate.now(), "TestPayment")
            assertFalse(populatedReceipts!!.deleteReceipt(nonExistentReceipt))
        }

        @Test
        fun `updateReceipt updates the specified receipt in the ArrayList`(){
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
        fun `updateReceipt returns false if the specified receipt is not in the ArrayList`(){
            val idToUpdate = 10
            val updatedReceipt = Receipt("UpdatedStore", "UpdatedCategory", "UpdatedDescription", LocalDate.now(), "UpdatedPayment")
            assertFalse(populatedReceipts!!.updateReceipt(idToUpdate, updatedReceipt))
        }

    }
}
