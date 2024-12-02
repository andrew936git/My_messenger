package com.example.my_messenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.my_messenger.login.LoginViewModel
import com.example.my_messenger.password_recovery.ForgotPasswordViewModel
import com.example.my_messenger.registration.RegisterViewModel

import com.google.firebase.auth.FirebaseAuth


class ViewModelFactory(
    private val firebaseAuth: FirebaseAuth
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(firebaseAuth) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(firebaseAuth) as T
            modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java) -> ForgotPasswordViewModel(firebaseAuth) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}