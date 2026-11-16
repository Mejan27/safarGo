package com.example.data.model

enum class NotificationType {
    BOOKING_CONFIRMED,
    DRIVER_ASSIGNED,
    DRIVER_ARRIVED,
    RIDE_STARTED,
    RIDE_COMPLETED,
    PAYMENT_SUCCESS,
    ADVANCE_REMINDER,
    SYSTEM_ALERT
}

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val relatedRideId: String? = null
)
