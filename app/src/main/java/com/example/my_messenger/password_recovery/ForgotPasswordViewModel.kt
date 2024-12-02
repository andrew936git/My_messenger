package com.example.my_messenger.password_recovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel(private val auth: FirebaseAuth) : ViewModel() {

    private val _isSendingEmail = MutableLiveData<Boolean>()
    val isSendingEmail: LiveData<Boolean> get() = _isSendingEmail

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _isEmailSent = MutableLiveData<Boolean>()
    val isEmailSent: LiveData<Boolean> get() = _isEmailSent

    fun sendResetEmail(email: String) {
        if (email.isEmpty()) {
            _error.value = "Введите email"
            return
        }

        _isSendingEmail.value = true
        _error.value = null

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                _isSendingEmail.value = false
                if (task.isSuccessful) {
                    _isEmailSent.value = true
                } else {
                    _error.value = task.exception?.message ?: "Ошибка отправки письма"
                }
            }
    }
}