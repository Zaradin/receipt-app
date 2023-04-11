package models

import java.util.Date

class Receipt ( var storeName: String,
                var category :String,
                var description :String,
                var dateOfReceipt :Date,
                var paymentMethod : String
){
}