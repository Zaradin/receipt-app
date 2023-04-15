package controllers

import models.Receipt
import persistence.Serializer
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToLong

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

    fun averageReceiptSpend(): Double {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
        val receiptsByWeek = receipts.groupBy {
            it.dateOfReceipt.format(DateTimeFormatter.ISO_WEEK_DATE)
        }
        val weeklySpend = receiptsByWeek.mapValues { (_, receipts) ->
            receipts.flatMap { receipt ->
                receipt.products.map { product ->
                    product.productPrice * product.quantityBought
                }
            }.sum()
        }
        val numberOfWeeks = weeklySpend.size
        val totalSpend = weeklySpend.values.sum()
        return totalSpend / numberOfWeeks.toDouble()
    }

    fun topCategoriesBySpend(): String {
        val categoriesToSpend = mutableMapOf<String, Double>()
        for (receipt in receipts) {
            for (product in receipt.products) {
                val category = receipt.category.lowercase(Locale.getDefault())
                val spend = receipt.totalSpendForReceipt()
                categoriesToSpend[category] = (categoriesToSpend[category] ?: 0.0) + spend
            }
        }

        val topCategories = categoriesToSpend.entries
            .sortedByDescending { it.value }
            .take(5)

        val result = StringBuilder()
        for ((category, spend) in topCategories) {
            result.append("$category : €${"%.2f".format(spend)}\n")
        }
        return result.toString()
    }

    fun paymentBreakdown(): String {
        val paymentTypes = receipts.flatMap { it.paymentMethod.split(", ") }
        val paymentTypeCounts = paymentTypes.groupingBy { it }.eachCount()
        val totalPayments = paymentTypes.size

        val paymentTypePercentages = paymentTypeCounts.mapValues { (_, count) ->
            (count.toDouble() / totalPayments) * 100
        }

        return paymentTypePercentages.map { (paymentType, percentage) ->
            "$paymentType: ${"%.2f".format(percentage)}%"
        }.joinToString("\n")
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