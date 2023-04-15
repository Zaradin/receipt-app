package controllers

import models.Receipt
import persistence.Serializer

class ReceiptAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType
    private var receipts = ArrayList<Receipt>()


    // Helper function for formatting receipts before being returned
    private fun formatListString(receiptsToFormat: List<Receipt>): String =
        receiptsToFormat.joinToString(separator = "\n") {receipt -> receipts.indexOf(receipt).toString() + ": " + receipt.toString()}


    // add receipt passed into the arraylist
    fun add(receipt: Receipt): Boolean {
        return receipts.add(receipt)
    }

    // list all receipts in arraylist
    fun listAllReceipts() =
        if(receipts.isEmpty()) "No receipts stored"
        else formatListString(receipts)

    fun numberOfReceipts(): Int {
        return receipts.size
    }

    fun findReceipt(index: Int): Receipt? {
        return if(isValidListIndex(index, receipts)){
            receipts[index]
        } else null
    }

    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    fun searchReceipts(searchTerm: String) =
        formatListString(receipts.filter { receipt -> receipt.storeName.contains(searchTerm, ignoreCase = true) })

    fun deleteReceipt(receiptToDelete: Receipt): Boolean {
        return receipts.remove(receiptToDelete)
    }

    fun updateReceipt(id: Int, updatedReceipt: Receipt): Boolean {
        val receiptToUpdate = findReceipt(id)
        if(receiptToUpdate != null){
            receiptToUpdate.storeName = updatedReceipt.storeName
            receiptToUpdate.category = updatedReceipt.category
            receiptToUpdate.description = updatedReceipt.description
            receiptToUpdate.dateOfReceipt = updatedReceipt.dateOfReceipt
            receiptToUpdate.paymentMethod = updatedReceipt.paymentMethod
            return true
        }
        return false
    }

    fun totalSpendForAllReceipts(): Double {
        return receipts.sumOf { it.totalSpendForReceipt() }
    }

    @Throws(Exception::class)
    fun load() {
        receipts = serializer.read() as ArrayList<Receipt>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(receipts)
    }


}