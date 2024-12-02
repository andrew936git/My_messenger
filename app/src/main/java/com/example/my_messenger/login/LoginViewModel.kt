package com.example.my_messenger.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel(private val auth: FirebaseAuth) : ViewModel() {

    private val _isLoggingIn = MutableLiveData<Boolean>()

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> get() = _isLoggedIn

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _error.value = "Введите email и пароль"
            return
        }

        _isLoggingIn.value = true
        _error.value = null

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoggingIn.value = false
                if (task.isSuccessful) {
                    _isLoggedIn.value = true
                } else {
                        _error.value = task.exception?.message ?: "Ошибка входа"
                    }
            }
    }
}