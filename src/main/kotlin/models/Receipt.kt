package models

import java.time.LocalDate

data class Receipt (var storeName: String,
               var category :String,
               var description :String,
               var dateOfReceipt : LocalDate,
               var paymentMethod : String
){
}