package controllers

import models.Receipt
import persistence.Serializer
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.collections.ArrayList

/**
 * Provides functionality for managing a collection of [Receipt]s, including adding, deleting, updating, and searching for receipts,
 * and generating various statistics about the collection.
 * @author Josh Crotty
 * @since 2.0
 *
 * @param serializerType The type of serializer to use for storing and loading the collection of receipts.
 */
class ReceiptAPI(serializerType: Serializer) {
    /**
     * The serializer used to store and load the collection of receipts.
     */
    private var serializer: Serializer = serializerType
    /**
     * The list of receipts managed by this [ReceiptAPI] instance.
     */
    private var receipts = ArrayList<Receipt>()

    /**
     * Helper function for formatting a list of receipts as a string for display.
     *
     * @param receiptsToFormat The list of receipts to format.
     * @return A string representation of the receipts.
     */
    private fun formatListString(receiptsToFormat: List<Receipt>): String =
        receiptsToFormat.joinToString(separator = "\n") { receipt -> receipts.indexOf(receipt).toString() + ": " + receipt.toString() }

    /**
     * Adds the given [Receipt] to the collection of receipts.
     *
     * @param receipt The receipt to add.
     * @return `true` if the receipt was successfully added, `false` otherwise.
     */
    fun add(receipt: Receipt): Boolean {
        return receipts.add(receipt)
    }

    /**
     * Lists all receipts in the collection as a string.
     *
     * @return A string representation of all the receipts in the collection.
     */
    fun listAllReceipts() =
        if (receipts.isEmpty()) "No receipts stored"
        else formatListString(receipts)

    /**
     * Returns the number of receipts in the collection.
     *
     * @return The number of receipts in the collection.
     */

    fun numberOfReceipts(): Int {
        return receipts.size
    }

    /**
     * Returns the receipt at the given index in the collection, or `null` if the index is out of bounds.
     *
     * @param index The index of the receipt to retrieve.
     * @return The receipt at the given index, or `null` if the index is out of bounds.
     */
    fun findReceipt(index: Int): Receipt? {
        return if (isValidListIndex(index, receipts)) {
            receipts[index]
        } else null
    }

    /**
     * Returns `true` if the given index is a valid index for the given list, `false` otherwise.
     *
     * @param index The index to check.
     * @param list The list to check the index against.
     * @return `true` if the index is valid, `false` otherwise.
     */
    private fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    /**
     * Returns a string representation of all receipts in the collection whose store name contains the given search term, ignoring case.
     *
     * @param searchTerm The search term to look for in the store names.
     * @return A string representation of all receipts whose store name contains the search term.
     */
    fun searchReceipts(searchTerm: String) =
        formatListString(receipts.filter { receipt -> receipt.storeName.contains(searchTerm, ignoreCase = true) })

    /**
     * Removes the given [Receipt] from the collection.
     *
     * @param receiptToDelete The [Receipt] to remove.
     * @return `true` if the receipt was successfully removed, `false` otherwise.
     */
    fun deleteReceipt(receiptToDelete: Receipt): Boolean {
        return receipts.remove(receiptToDelete)
    }

    /**
     * Updates the [Receipt] with the given [id] with the provided [updatedReceipt].
     *
     * @param id The ID of the receipt to update.
     * @param updatedReceipt The updated receipt object to replace the existing receipt.
     * @return `true` if the receipt was successfully updated, `false` otherwise.
     */
    fun updateReceipt(id: Int, updatedReceipt: Receipt): Boolean {
        val receiptToUpdate = findReceipt(id)
        if (receiptToUpdate != null) {
            receiptToUpdate.storeName = updatedReceipt.storeName
            receiptToUpdate.category = updatedReceipt.category
            receiptToUpdate.description = updatedReceipt.description
            receiptToUpdate.dateOfReceipt = updatedReceipt.dateOfReceipt
            receiptToUpdate.paymentMethod = updatedReceipt.paymentMethod
            return true
        }
        return false
    }

    /**
     * Returns the total spend for all receipts.
     * @return The total spend for all receipts.
     */
    fun totalSpendForAllReceipts(): Double {
        return receipts.sumOf { it.totalSpendForReceipt() }
    }

    /**
     * Calculates the average spend per receipt for all receipts in the collection.
     *
     * @return The average spend per receipt.
     */
    fun averageReceiptSpend(): Double {
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

    /**
     * Calculates the top 5 categories by spend across all receipts in the collection.
     *
     * @return A string containing the top categories and their total spend formatted as "category : €X.XX\n".
     */
    fun topCategoriesBySpend(): String {
        val categoriesToSpend = mutableMapOf<String, Double>()
        for (receipt in receipts) {
            val category = receipt.category.lowercase(Locale.getDefault())
            val spend = receipt.totalSpendForReceipt()
            categoriesToSpend[category] = (categoriesToSpend[category] ?: 0.0) + spend
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

    /**
     * Calculates the breakdown of payment types used for all receipts in the collection.
     *
     * @return A string containing the payment types and their percentages formatted as "paymentType: X.XX%".
     */
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

    /**
     * Loads the collection of receipts from a file using the serializer.
     *
     * @throws Exception if there is an error during reading from file.
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(Exception::class)
    fun load() {
        receipts = serializer.read() as ArrayList<Receipt>
    }


    /**
     * Stores the collection of receipts to a file using the serializer.
     *
     * @throws Exception if there is an error during writing to file.
     */
    @Throws(Exception::class)
    fun store() {
        serializer.write(receipts)
    }
}
