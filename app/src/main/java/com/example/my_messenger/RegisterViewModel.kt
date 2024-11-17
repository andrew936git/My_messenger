package com.example.my_messenger


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel(): ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> get() = _registerSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun register(email: String, password: String, confirmPassword: String) {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _errorMessage.value = "Все поля должны быть заполнены"
            return
        }

        if (password != confirmPassword) {
            _errorMessage.value = "Пароли не совпадают"
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    val userData = hashMapOf(
                        "email" to email,
                        "nickname" to "",
                        "avatarUrl" to ""
                    )
                    firestore.collection("users").document(userId).set(userData)
                        .addOnSuccessListener {
                            _registerSuccess.value = true
                        }
                        .addOnFailureListener {
                            _errorMessage.value = "Ошибка сохранения данных: ${it.localizedMessage}"
                        }
                } else {
                    _errorMessage.value = task.exception?.localizedMessage
                }
            }
    }
}