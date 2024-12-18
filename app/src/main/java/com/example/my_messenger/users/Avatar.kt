package com.example.my_messenger.users

import com.example.my_messenger.R

data class Avatar(
    var id: String = "",
    var position: Int = 0
) {
    companion object {
        val avatars = listOf(
            R.drawable.avatar_1,
            R.drawable.avatar_2,
            R.drawable.avatar_3,
            R.drawable.avatar_4,
            R.drawable.avatar_5,
        )
    }
}