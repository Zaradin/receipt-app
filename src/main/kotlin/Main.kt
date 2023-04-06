import utils.ScannerInput

fun main(args: Array<String>) {
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
         > |   5)                           |
         > |   6) Search Receipts           |
         > |   7) Save Receipts             |
         > |   8) Load Receipts             |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">"))
}

fun runmenu() {
    do {
        val option = mainMenu()
        when(option){
            //1 -> addReceipt()
            //2 -> listReceipts()
            //3 -> updateReceipt()
            //4 -> deleteReceipt()
            0 -> exitApp()
            else -> println("invalid option entered: ${option}")
        }
    } while(true)
}

fun exitApp(){
    println("Exiting...bye")
    System.exit(0)
}
