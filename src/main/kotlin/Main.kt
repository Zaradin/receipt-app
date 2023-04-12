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
         > |   8) Delete Product in Receipt |
         > |   9) Number of Products        |
         > |   10) Update Product Info      |
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
            2 -> println(listReceipts())
            3 -> updateReceipt()
            4 -> deleteReceipt()
            5 -> searchReceipts()
            6 -> addProductToReceipt()
            7 -> listProductsInReceipt()
            8 -> deleteProductInReceipt()
            9 -> numberOfProducts()
            10 -> updateProduct()
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

fun listReceipts(): String {
    //logger.info { "listReceipts() function invoked" }

    return receiptAPI.listAllReceipts()
}

fun updateReceipt(){
    logger.info { "updateReceipt() function invoked" }
    val receipts = listReceipts()
    println(receipts)
    if (receipts == "No receipts stored") {
        return
    }

    val receiptIndex = readNextInt("Enter the index of the receipt you want to update: ")

    val storeName = readNextLine("Enter the new store name for the receipt: ")
    val receiptCategory = readNextLine("Enter the new category of receipt: ")
    val description = readNextLine("Enter the new receipt description: ")
    val dateOfReceipt = LocalDate.parse(readNextLine("Enter the new date of the receipt, (13/04/23): "), formatter)
    val paymentMethod = readNextLine("Enter the new payment method, (cash, card): ")

    if(receiptAPI.updateReceipt(receiptIndex, Receipt(storeName = storeName, category = receiptCategory, description = description, dateOfReceipt = dateOfReceipt, paymentMethod = paymentMethod))){
        println("Receipt updated!")
    }

}

fun deleteReceipt(){
    //logger.info { "deleteReceipt() function invoked" }
    val receipts = listReceipts()
    println(listReceipts())
    if (receipts == "No receipts stored") {
        return
    }
    val receipt: Receipt? = receiptAPI.findReceipt(readNextInt("Enter the index of the receipt you want to delete: "))

    if (receipt != null) {
        if(receiptAPI.deleteReceipt(receipt)){
            println("Receipt Deleted!")
        }
    }
}

private fun addProductToReceipt() {
    println(listReceipts())
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

private fun listProductsInReceipt() {
    val receipts = listReceipts()
    println(receipts)
    if (receipts == "No receipts stored") {
        return
    }
    val receipt = receiptAPI.findReceipt(readNextInt("Enter the index of the receipt to list products: "))
    if (receipt != null) {
        println(receipt.listProducts())
    }
}


private fun deleteProductInReceipt(){
    println(listReceipts())
    val receipt: Receipt? = receiptAPI.findReceipt(readNextInt("Enter the index of the receipt to delete product: "))
    if(receipt != null) {
        println(receipt.listProducts())
        if(receipt.deleteProduct(readNextInt("Enter the index of the product you want to delete: "))){
            println("Product deleted!")
        }
    }
}

private fun numberOfProducts(){
    println(listReceipts())
    val receipt: Receipt? = receiptAPI.findReceipt(readNextInt("Enter the index of the receipt to get number of products: "))

    if(receipt != null){
        println(receipt.numberOfProducts())
    }
}

private fun updateProduct(){
    // TODO:  Fix function so that it doesn't prompt user if there are no receipts or products in data store
    println(listReceipts())
    val receipt: Receipt? = receiptAPI.findReceipt(readNextInt("Enter the index of the receipt to update a product: "))

    println(receipt?.listProducts())

    var productIndex: Int = readNextInt("Enter the index of the product you would like to update: ")
    var newProductName = readNextLine("Enter the new product name: ")
    var newProductPrice = readNextDouble("Enter the new price of the product: ")
    var newQuantityBought = readNextInt("Enter the new quantity bought: ")

    if(receipt?.updateProduct(productIndex, Product(productName = newProductName, productPrice = newProductPrice, quantityBought = newQuantityBought)) == true){
        println("Product Updated!")
    }
}

private fun searchReceipts(){
    if(receiptAPI.numberOfReceipts() == 0){
        println("No receipts stored to search")
        return
    }
    val searchTerm = readNextLine("Enter store name to search receipts: ")
    println(receiptAPI.searchReceipts(searchTerm))
}


