package com.example.my_messenger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserListViewModel : ViewModel() {

    private val allUsers = listOf(
        User("1", "User 1"),
        User("2", "User 2"),
        User("3", "User 3")
    )

    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> get() = _userList

    init {
        _userList.value = allUsers
    }

    fun search(query: String) {
        _userList.value = allUsers.filter { it.name.contains(query, ignoreCase = true) }
    }
}

data class User(val id: String, val name: String)