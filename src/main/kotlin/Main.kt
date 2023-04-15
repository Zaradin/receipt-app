import controllers.ReceiptAPI
import models.Product
import models.Receipt
import mu.KotlinLogging
import persistence.XMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextDouble
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.system.exitProcess

/**
 * This is the main class for the receipt-app and the starting point of execution of the application.
 * This holds the menus and user input, along with printing to the console.
 * @author Josh Crotty
 * @since 2.0
 */

private val logger = KotlinLogging.logger {}

/**
 * Creates a new instance of ReceiptAPI with an XMLSerializer or JSONSerializer
 * depending on the file extension of the specified file.
 *
 * @param file the file containing the serialized receipts
 */
private val receiptAPI = ReceiptAPI(XMLSerializer(File("receipts.xml")))
// private val receiptAPI = ReceiptAPI(JSONSerializer(File("receipts.json")))

/**
 * The formatter used for displaying date and time information.
 */
private val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")

fun main() {
    runmenu()
}

/**
 * Displays the main menu and returns the user's selection as an integer.
 *
 * @return the user's menu selection
 */
fun mainMenu(): Int {
    return ScannerInput.readNextInt(
        """ 
         > ----------------------------------
         > |         RECEIPT TRACKER        |
         > ----------------------------------
         > | RECEIPT MENU                   |
         > |   1) Add a receipt             |
         > |   2) List receipts             |
         > |   3) Update a receipt          |
         > |   4) Delete a receipt          |
         > |   5) Search Receipts           |
         > ----------------------------------
         > | PRODUCT MENU                   |
         > |   6) Add Product to Receipt    |
         > |   7) List Products in Receipt  |
         > |   8) Delete Product in Receipt |
         > |   9) Number of Products        |
         > |   10) Update Product Info      |
         > ----------------------------------
         > | SPENDING ANALYSIS MENU         |
         > |   11) Open Menu                |
         > ----------------------------------
         > |   20) Save Receipts            |
         > |   21) Load Receipts            |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">")
    )
}

/**
 * Displays the spending analysis sub-menu and returns the user's selection as an integer.
 *
 * @return the user's menu selection
 */
fun spendingSubMenu(): Int {
    return ScannerInput.readNextInt(
        """ 
         > ----------------------------------
         > |     SPENDING ANALYSIS MENU     |
         > ----------------------------------
         > | SPENDING SUB-MENU              |
         > |   1) Total Spending            |
         > |   2) Average Receipt Spend     |
         > |   3) Top 5 categories of spend |
         > |   4) Payment Type Breakdown    |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">")
    )
}

/**
 * Runs the main menu until the user exits the application.
 */
fun runmenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addReceipt()
            2 -> println(listReceipts())
            3 -> updateReceipt()
            4 -> deleteReceipt()
            5 -> searchReceipts()
            6 -> addProductToReceipt()
            7 -> listProductsInReceipt()
            8 -> deleteProductInReceipt()
            9 -> numberOfProducts()
            10 -> updateProduct()
            11 -> runSpendingSubMenu()
            20 -> save()
            21 -> load()
            0 -> exitApp()
            else -> println("invalid option entered: $option")
        }
    } while (true)
}

/**
 * Runs the spending analysis sub-menu until the user returns to the main menu.
 */
fun runSpendingSubMenu() {
    do {
        when (val option = spendingSubMenu()) {
            1 -> totalSpending()
            2 -> averageReceiptSpend()
            3 -> topCategoriesOfSpend()
            4 -> paymentBreakdown()
            0 -> runmenu()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}

/**
 * Exits the application.
 */
fun exitApp() {
    println("Exiting...bye")
    exitProcess(0)
}

/**
 * Checks if there are any receipts in the system.
 * @return `true` if there are receipts, `false` otherwise.
 */
fun checkReceipts(): Boolean {
    val receipts = listReceipts()
    if (receipts == "No receipts stored") {
        println(receipts)
        return false
    }
    println(receipts)
    return true
}

/**
 * Adds a receipt to the collection.
 */
fun addReceipt() {
    // logger.info{"addReceipt() function invoked"}

    val storeName = readNextLine("Enter the store name for the receipt: ")
    val receiptCategory = readNextLine("Enter the category of receipt: ")
    val description = readNextLine("Enter the receipt description: ")
    val dateOfReceipt = LocalDate.parse(readNextLine("Enter the date of the receipt, (13/04/23): "), formatter)
    val paymentMethod = readNextLine("Enter the payment method, (cash, card): ")

    val isAdded = receiptAPI.add(Receipt(storeName, receiptCategory, description, dateOfReceipt, paymentMethod))

    if (isAdded) {
        println("Receipt Added Successfully")
    } else {
        println("Add Failed")
    }
}

/**
 * Lists all the receipts in the collection.
 * @return A string containing all the receipts.
 */
fun listReceipts(): String {
    // logger.info { "listReceipts() function invoked" }

    return receiptAPI.listAllReceipts()
}

/**
 * Updates a receipt in the collection.
 */
fun updateReceipt() {
    logger.info { "updateReceipt() function invoked" }
    if (!checkReceipts()) {
        return
    }

    val receiptIndex = readNextInt("Enter the index of the receipt you want to update: ")

    val storeName = readNextLine("Enter the new store name for the receipt: ")
    val receiptCategory = readNextLine("Enter the new category of receipt: ")
    val description = readNextLine("Enter the new receipt description: ")
    val dateOfReceipt = LocalDate.parse(readNextLine("Enter the new date of the receipt, (13/04/23): "), formatter)
    val paymentMethod = readNextLine("Enter the new payment method, (cash, card): ")

    if (receiptAPI.updateReceipt(receiptIndex, Receipt(storeName = storeName, category = receiptCategory, description = description, dateOfReceipt = dateOfReceipt, paymentMethod = paymentMethod))) {
        println("Receipt updated!")
    }
}

/**
 * Deletes a receipt from the collection.
 */
fun deleteReceipt() {
    // logger.info { "deleteReceipt() function invoked" }
    if (!checkReceipts()) {
        return
    }
    val receipt: Receipt? = receiptAPI.findReceipt(readNextInt("Enter the index of the receipt you want to delete: "))

    if (receipt != null) {
        if (receiptAPI.deleteReceipt(receipt)) {
            println("Receipt Deleted!")
        }
    }
}

/**
 * Adds a product to a receipt.
 * If the receipt doesn't exist, the function returns without doing anything.
 * If the product is added successfully, a success message is printed.
 */
private fun addProductToReceipt() {
    if (!checkReceipts()) {
        return
    }
    val receipt: Receipt? = receiptAPI.findReceipt(readNextInt("Enter the index of the receipt to add a product: "))

    if (receipt != null) {
        val productName = readNextLine("Enter the product name: ")
        val productPrice = readNextDouble("Enter the price of the product: ")
        val quantityBought = readNextInt("Enter the quantity bought: ")
        if (receipt.addProduct(Product(productName = productName, productPrice = productPrice, quantityBought = quantityBought))) {
            println("Product added Successfully!")
        }
    }
}

/**
 * Lists all products in a receipt.
 * If there are no receipts in the system, returns early.
 * Prompts the user to enter the index of the receipt to list the products for.
 * Prints the list of products for that receipt if it exists.
 */
private fun listProductsInReceipt() {
    if (!checkReceipts()) {
        return
    }
    val receipt = receiptAPI.findReceipt(readNextInt("Enter the index of the receipt to list products: "))
    if (receipt != null) {
        println(receipt.listProducts())
    }
}

/**
 * Deletes a product from a receipt.
 * Prompts the user to enter the index of the receipt to delete a product, and the index of the product to delete.
 * @return Unit
 */
private fun deleteProductInReceipt() {
    if (!checkReceipts()) {
        return
    }
    val receipt: Receipt? = receiptAPI.findReceipt(readNextInt("Enter the index of the receipt to delete product: "))
    if (receipt != null) {
        println(receipt.listProducts())
        if (receipt.deleteProduct(readNextInt("Enter the index of the product you want to delete: "))) {
            println("Product deleted!")
        }
    }
}

/**
 * Displays the number of products in a receipt.
 * Prompts the user to enter the index of the receipt to get the number of products, and prints the number of products.
 * @return Unit
 */
private fun numberOfProducts() {
    if (!checkReceipts()) {
        return
    }
    val receipt: Receipt? = receiptAPI.findReceipt(readNextInt("Enter the index of the receipt to get number of products: "))

    if (receipt != null) {
        println(receipt.numberOfProducts())
    }
}

/**
 * Updates a product in a receipt.
 * Prompts the user to enter the index of the receipt to update a product, the index of the product to update,
 * and the new name, price and quantity of the product.
 * @return Unit
 */
private fun updateProduct() {
    if (!checkReceipts()) {
        return
    }
    val receipt: Receipt? = receiptAPI.findReceipt(readNextInt("Enter the index of the receipt to update a product: "))

    println(receipt?.listProducts())

    val productIndex: Int = readNextInt("Enter the index of the product you would like to update: ")
    val newProductName = readNextLine("Enter the new product name: ")
    val newProductPrice = readNextDouble("Enter the new price of the product: ")
    val newQuantityBought = readNextInt("Enter the new quantity bought: ")

    if (receipt?.updateProduct(productIndex, Product(productName = newProductName, productPrice = newProductPrice, quantityBought = newQuantityBought)) == true) {
        println("Product Updated!")
    }
}

/**
 * Searches receipts for a given store name and prints the matching receipts.
 * If no receipts match, prints a message indicating so.
 */
private fun searchReceipts() {
    if (!checkReceipts()) {
        return
    }
    val searchTerm = readNextLine("Enter store name to search receipts: ")
    println(receiptAPI.searchReceipts(searchTerm))
}

/**
 * Prints the total amount spent across all receipts.
 */
private fun totalSpending() {
    println("Total spend: €${receiptAPI.totalSpendForAllReceipts()}")
}

/**
 * Prints the average spend per receipt.
 */
private fun averageReceiptSpend() {
    println("Average Receipt Spend: €${receiptAPI.averageReceiptSpend()}")
}

/**
 * Prints the top 5 categories by amount spent.
 */
private fun topCategoriesOfSpend() {
    println("Top 5 categories of spend: ")
    println(receiptAPI.topCategoriesBySpend())
}

/**
 * Prints a breakdown of payments by payment type.
 */
private fun paymentBreakdown() {
    println(receiptAPI.paymentBreakdown())
}

/**
 * Saves the current state of the receipt API to a file.
 * If an error occurs, prints an error message to standard error.
 */
fun save() {
    try {
        receiptAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

/**
 * Loads the receipt API state from a file.
 * If an error occurs, prints an error message to standard error.
 */
fun load() {
    try {
        receiptAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}
