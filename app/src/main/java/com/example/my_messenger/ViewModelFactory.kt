package com.example.my_messenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth


class ViewModelFactory(
    private val firebaseAuth: FirebaseAuth
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(firebaseAuth) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(firebaseAuth) as T
            modelClass.isAssignableFrom(ChatListViewModel::class.java) -> ChatListViewModel() as T
            modelClass.isAssignableFrom(UserListViewModel::class.java) -> UserListViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}