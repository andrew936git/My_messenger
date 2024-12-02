package com.example.my_messenger.registration


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel(private val auth: FirebaseAuth) : ViewModel() {

    private val _isRegistering = MutableLiveData<Boolean>()


    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _isRegistered = MutableLiveData<Boolean>()
    val isRegistered: LiveData<Boolean> get() = _isRegistered

    fun register(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _error.value = "Введите email и пароль"
            return
        }

        _isRegistering.value = true
        _error.value = null

        val name = auth.currentUser

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isRegistering.value = false
                if (task.isSuccessful) {
                    _isRegistered.value = true
                } else {
                    _error.value = task.exception?.message ?: "Ошибка регистрации"
                }
            }
    }
}