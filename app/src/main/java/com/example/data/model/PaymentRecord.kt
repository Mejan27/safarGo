package com.example.data.model

enum class PaymentGateway(val displayName: String, val feePercent: Double) {
    BKASH("bKash (বিকাশ)", 1.5),
    NAGAD("Nagad (নগদ)", 1.2),
    ROCKET("Rocket (রকেট)", 1.5),
    CARD("Visa / Mastercard", 2.0),
    CASH("Cash to Driver", 0.0)
}

data class PaymentRecord(
    val transactionId: String,
    val rideId: String,
    val passengerName: String,
    val amount: Double,
    val gateway: PaymentGateway,
    val accountNumber: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "SUCCESS",
    val receiptNumber: String = ""
)
