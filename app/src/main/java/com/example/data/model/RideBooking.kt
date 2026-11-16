package com.example.data.model

enum class RideStatus {
    PENDING,
    ACCEPTED,
    DRIVER_ARRIVED,
    IN_TRANSIT,
    COMPLETED,
    CANCELLED
}

enum class PaymentStatus {
    UNPAID,
    PAID_BKASH,
    PAID_NAGAD,
    PAID_CARD,
    PAID_CASH
}

enum class VehicleCategory(
    val labelEn: String,
    val labelBn: String,
    val baseRate: Double,
    val perKmRate: Double,
    val capacity: Int,
    val typeName: String
) {
    BATTERY_AUTO(
        "RCC Electric Auto-Rickshaw",
        "সিসিক অনুমোদিত ব্যাটারি অটো",
        15.0,
        15.0,
        5,
        "অটোরিকশা"
    ),
    CHARGER_RICKSHAW(
        "Battery Charger Rickshaw",
        "চার্জার ব্যাটারি রিকশা",
        15.0,
        15.0,
        2,
        "রিকশা"
    ),
    CAMPUS_STUDENT_AUTO(
        "Campus Student Special (RU/RUET)",
        "ক্যাম্পাস শিক্ষার্থী স্পেশাল অটো",
        15.0,
        15.0,
        5,
        "ক্যাম্পাস অটো"
    ),
    PADDLE_RICKSHAW(
        "Traditional Pedal Rickshaw",
        "ঐতিহ্যবাহী প্যাডেল রিকশা",
        15.0,
        15.0,
        2,
        "রিকশা"
    ),
    FAMILY_RESERVE_AUTO(
        "Family Reserve Auto",
        "ফ্যামিলি রিজার্ভ অটোরিকশা",
        15.0,
        15.0,
        5,
        "রিজার্ভ অটো"
    )
}

data class RideBooking(
    val id: String,
    val bookingReference: String,
    val passengerId: String,
    val passengerName: String,
    val passengerPhone: String,
    val driverId: String = "",
    val driverName: String = "",
    val driverPhone: String = "",
    val driverTokenNumber: String = "সিসিক টোকেন: লাল-০৮",
    val vehicleModel: String = "",
    val vehiclePlate: String = "",
    val vehicleCategory: VehicleCategory = VehicleCategory.BATTERY_AUTO,
    val operatingZone: String = "রাজশাহী মহানগর - সাহেব বাজার ও জিরো পয়েন্ট এলাকা",
    val pickupLocation: String,
    val dropoffLocation: String,
    val distanceKm: Double,
    val estimatedMinutes: Int,
    val fare: Double,
    val isAdvanceBooking: Boolean = true,
    val isReserve: Boolean = false,
    val scheduledTimeMillis: Long,
    val bookedAtMillis: Long = System.currentTimeMillis(),
    val status: RideStatus = RideStatus.PENDING,
    val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
    val transactionId: String = "",
    val paymentMethod: String = "",
    val passengerCount: Int = 1,
    val luggageDetails: String = "বাজারের ব্যাগ ও ব্যক্তিগত মালামাল",
    val driverRating: Float = 4.9f,
    val specialNotes: String = "",
    val progressFraction: Float = 0f,
    val qrCodeData: String = ""
)
