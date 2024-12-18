package com.example.my_messenger.registration


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_messenger.users.Avatar
import com.example.my_messenger.users.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class RegisterViewModel(private val auth: FirebaseAuth) : ViewModel() {

    private val _isRegistering = MutableLiveData<Boolean>()
    private val database = FirebaseFirestore.getInstance()

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _isRegistered = MutableLiveData<Boolean>()
    val isRegistered: LiveData<Boolean> get() = _isRegistered
    private val firestore = FirebaseFirestore.getInstance()
    private val avatarCollection = firestore.collection("avatar")

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
        val random = Random
        val currentUser = auth.currentUser
        currentUser?.let {
            val displayName = it.email?.trim('@') ?: ""
            val user = User(
                id = it.uid,
                it.email!!,
                displayName
            )

            val avatar = Avatar(
                it.uid,
                random.nextInt(0, 4)
            )
            avatarCollection.document(avatar.id).set(avatar)
            database.collection("avatars").document(it.uid)
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