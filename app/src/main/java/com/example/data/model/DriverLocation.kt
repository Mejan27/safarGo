package com.example.data.model

data class DriverLocation(
    val driverId: String,
    val driverName: String,
    val latitude: Double,
    val longitude: Double,
    val speedKmh: Float,
    val headingDegrees: Float,
    val isSharingLocation: Boolean = true,
    val vehiclePlate: String = "",
    val driverTokenNumber: String = "টোকেন নং: ১২",
    val vehicleType: String = "ব্যাটারি চালিত রিকশা",
    val driverPhone: String = "",
    val currentZone: String = "মিরপুর-পল্লবী লোকাল জোন",
    val nearbyStand: String = "কাঁচাবাজার মোড় স্ট্যান্ড",
    val lastUpdatedMillis: Long = System.currentTimeMillis()
)
