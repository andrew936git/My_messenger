package com.example.my_messenger.users

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val stringData: MutableLiveData<String> = MutableLiveData()
}