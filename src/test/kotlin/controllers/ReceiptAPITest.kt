package controllers

import models.Receipt
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class ReceiptAPITest {

    private var groceryReceipt: Receipt? = null
    private var clothingReceipt: Receipt? = null
    private var electronicsReceipt: Receipt? = null
    private var populatedReceipts: ReceiptAPI? = ReceiptAPI()
    private var emptyReceipts: ReceiptAPI? = ReceiptAPI()

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
