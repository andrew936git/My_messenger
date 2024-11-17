package com.example.my_messenger

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ChatListFragment()
            1 -> UserListFragment()
            else -> throw IllegalStateException("Invalid tab position")
        }
    }
}