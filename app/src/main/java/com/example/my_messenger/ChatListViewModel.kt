package com.example.my_messenger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatListViewModel : ViewModel() {

    private val _chatList = MutableLiveData<List<Chat>>()
    val chatList: LiveData<List<Chat>> get() = _chatList

    init {
        loadChats()
    }

    private fun loadChats() {
        // Заглушка на время
        _chatList.value = listOf(
            Chat("1", "Чат с User 1"),
            Chat("2", "Чат с User 2"),
            Chat("3", "Чат с User 3")
        )
    }
}

data class Chat(val id: String, val name: String)