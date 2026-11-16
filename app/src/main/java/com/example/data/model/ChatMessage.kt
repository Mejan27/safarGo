package com.example.data.model

data class ChatMessage(
    val id: String,
    val rideId: String,
    val senderId: String,
    val senderRole: UserRole,
    val senderName: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)
