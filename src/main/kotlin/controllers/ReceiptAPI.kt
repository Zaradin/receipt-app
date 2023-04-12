package controllers

import models.Receipt

class ReceiptAPI {
    private var receipts = ArrayList<Receipt>()


    // Helper function for formatting receipts before being returned
    private fun formatListString(receiptsToFormat: List<Receipt>): String =
        receiptsToFormat.joinToString(separator = "\n") {receipt -> receipts.indexOf(receipt).toString() + ": " + receipt.toString()}


    // add receipt passed into the arraylist
    fun add(receipt: Receipt): Boolean {
        return receipts.add(receipt)
    }

    fun listAllReceipts(): String {
        return if (receipts.isEmpty()) {
            "No receipts stored"
        } else {
            var listOfReceipts = ""
            for(i in receipts.indices) {
                listOfReceipts += "${i}: ${receipts[i].toString()} \n"
            }
            listOfReceipts
        }
    }

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
}