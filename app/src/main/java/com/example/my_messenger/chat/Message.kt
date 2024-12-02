package com.example.my_messenger.chat

import java.util.Calendar
import java.util.Date

data class Message(
    val id: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val senderId: String = ""
)