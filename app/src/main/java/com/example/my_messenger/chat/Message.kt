package com.example.my_messenger.chat

data class Message(
    val id: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val senderId: String = ""
)