package com.example.my_messenger.users

data class User(
    var id: String = "",
    var email: String = "",
    var displayName: String = "",
    var name: String = "",
    var surname: String = "",
    var craft: String = "",
    var age: String = "",
    var city: String = "",
)
{
    companion object{
        var myAvatar: String = ""
    }
}
