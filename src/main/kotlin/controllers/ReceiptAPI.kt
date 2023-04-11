package controllers

import models.Receipt

class ReceiptAPI {
    private var receipts = ArrayList<Receipt>()


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
}