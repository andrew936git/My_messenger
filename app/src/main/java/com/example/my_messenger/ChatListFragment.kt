package com.example.my_messenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class ChatListFragment : Fragment() {
    private lateinit var viewModel: ChatListViewModel
    private lateinit var adapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_chat_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ChatAdapter { chatId ->
            val action = MainFragmentDirections.actionMainFragmentToChatListFragment(chatId)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
        val firebaseAuth = FirebaseAuth.getInstance()
        val factory = ViewModelFactory(firebaseAuth)
        viewModel = ViewModelProvider(this, factory)[ChatListViewModel::class.java]

        viewModel.chatList.observe(viewLifecycleOwner) { chatList ->
            adapter.submitList(chatList)
        }

        return view
    }
}