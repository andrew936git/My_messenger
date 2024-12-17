package com.example.my_messenger.chat

import java.io.Serializable

data class Message(
    var id: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val senderId: String = "",
    val recipientId: String = ""
)


data class User(
    var id: String = "",
    var email: String = "",
    var displayName: String = "",
    var name: String = "",
    var surname: String = "",
    var craft: String = "",
    var age: String = "",
    var city: String = ""
    ){
    companion object{
        val list = listOf(
            ""
        )
    }
}