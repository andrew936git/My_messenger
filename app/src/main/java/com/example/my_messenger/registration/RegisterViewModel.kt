package com.example.my_messenger.registration


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_messenger.chat.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel(private val auth: FirebaseAuth) : ViewModel() {

    private val _isRegistering = MutableLiveData<Boolean>()
    private val database = FirebaseFirestore.getInstance()

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

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isRegistering.value = false
                if (task.isSuccessful) {
                    addUserToDatabase()
                    _isRegistered.value = true
                } else {
                    _error.value = task.exception?.message ?: "Ошибка регистрации"
                }
            }
    }

    private fun addUserToDatabase() {
        val currentUser = auth.currentUser
        currentUser?.let {
            val displayName = it.email?.trim('@') ?: ""
            val user = User(
                id = it.uid,
                it.email!!,
                displayName
            )
            database.collection("users").document(it.uid)
                .set(user)
                .addOnSuccessListener {
                    Log.d("Firestore", "User successfully saved!")
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error saving user", e)
                }
        }
    }
}