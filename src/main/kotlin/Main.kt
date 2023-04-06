import mu.KotlinLogging
import utils.ScannerInput

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    runmenu()
}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |         RECEIPT TRACKER        |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add a receipt             |
         > |   2) List receipts             |
         > |   3) Update a receipt          |
         > |   4) Delete a receipt          |
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
    logger.info{"addReceipt() function invoked"}
}

fun listReceipts() {
    logger.info { "listReceipts() function invoked" }
}

fun updateReceipt(){
    logger.info { "updateReceipt() function invoked" }
}

fun deleteReceipt(){
    logger.info { "deleteReceipt() function invoked" }
}