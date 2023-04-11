import controllers.ReceiptAPI
import models.Product
import models.Receipt
import mu.KotlinLogging
import utils.ScannerInput
import utils.ScannerInput.readNextDouble
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val logger = KotlinLogging.logger {}

private val receiptAPI = ReceiptAPI()

private val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")

fun main(args: Array<String>) {
    runmenu()
}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
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
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">"))
}

fun runmenu() {
    do {
        val option = mainMenu()
        when(option){
            1 -> addReceipt()
            2 -> listReceipts()
            3 -> updateReceipt()
            4 -> deleteReceipt()
            //5 ->
            6 -> addProductToReceipt()
            7 -> listProductsInReceipt()
            0 -> exitApp()
            else -> println("invalid option entered: ${option}")
        }
    } while(true)
}

fun exitApp(){
    println("Exiting...bye")
    System.exit(0)
}


fun addReceipt() {
    //logger.info{"addReceipt() function invoked"}

    val storeName = readNextLine("Enter the store name for the receipt: ")
    val receiptCategory = readNextLine("Enter the category of receipt: ")
    val description = readNextLine("Enter the receipt description: ")
    val dateOfReceipt = LocalDate.parse(readNextLine("Enter the date of the receipt, (13/04/23): "), formatter)
    val paymentMethod = readNextLine("Enter the payment method, (cash, card): ")

    val isAdded = receiptAPI.add(Receipt(storeName, receiptCategory, description, dateOfReceipt, paymentMethod))

    if(isAdded){
        println("Receipt Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listReceipts() {
    //logger.info { "listReceipts() function invoked" }

    println(receiptAPI.listAllReceipts())
}

fun updateReceipt(){
    logger.info { "updateReceipt() function invoked" }
}

fun deleteReceipt(){
    logger.info { "deleteReceipt() function invoked" }
}

private fun addProductToReceipt() {
    listReceipts()
    val receipt: Receipt? = receiptAPI.findReceipt(readNextInt("Enter the index of the receipt to add a product: "))

    if(receipt != null){
        var productName = readNextLine("Enter the product name: ")
        var productPrice = readNextDouble("Enter the price of the product: ")
        var quantityBought = readNextInt("Enter the quantity bought: ")
        if(receipt.addProduct(Product(productName = productName, productPrice = productPrice, quantityBought = quantityBought))){
            println("Product added Successfully!")
        }
    }
}

private fun listProductsInReceipt(){
    listReceipts()
    val receipt: Receipt? = receiptAPI.findReceipt(readNextInt("Enter the index of the receipt to list products: "))

    if( receipt != null){
        println(receipt.listProducts())
    }
}