package com.example.my_messenger.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.my_messenger.chat.ChatListFragment
import com.example.my_messenger.users.UserListFragment

class PagerAdapter(fragmentActivity: FragmentActivity):
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UserListFragment()
            1 -> ChatListFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }

}