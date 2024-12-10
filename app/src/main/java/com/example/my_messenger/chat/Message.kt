package com.example.my_messenger.chat

data class Message(
    var id: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val senderId: String = "",
    val recipientId: String = ""
)


data class User(var email: String = "", var displayName: String = "")