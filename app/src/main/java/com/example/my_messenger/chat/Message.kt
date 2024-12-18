package com.example.my_messenger.chat

import com.example.my_messenger.R

data class Message(
    var id: String = "",
    var message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val senderId: String = "",
    val recipientId: String = "",

    ){
    companion object{
        val emoji = listOf(
            R.drawable.emoji_1,
            R.drawable.emoji_2,
            R.drawable.emoji_3,
            R.drawable.emoji_4,
            R.drawable.emoji_5,
            R.drawable.emoji_6,
            R.drawable.emoji_7,
            R.drawable.emoji_8,
            R.drawable.emoji_9,
        )
    }
}



