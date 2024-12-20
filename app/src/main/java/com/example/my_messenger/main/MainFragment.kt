package com.example.my_messenger.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.my_messenger.R
import com.example.my_messenger.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.viewPager.adapter = PagerAdapter(requireActivity())
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Пользователи"
                1 -> "Чаты"
                else -> null
            }
        }.attach()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.apply {
            inflateMenu(R.menu.main_menu)
            menu.apply {
                findItem(R.id.profile).isVisible = true
                findItem(R.id.about).isVisible = false
                findItem(R.id.exit).isVisible = true
                findItem(R.id.exit).setOnMenuItemClickListener {
                    val fileOutputStream =
                        activity?.openFileOutput("loginpasswordfile.txt", Context.MODE_PRIVATE)
                    fileOutputStream?.close()
                    findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
                    true
                }

                findItem(R.id.profile).setOnMenuItemClickListener {
                    findNavController().navigate(R.id.action_mainFragment_to_profileFragment)
                    true
                }
            }
            setTitle("Мессенджер")
        }
    }


}