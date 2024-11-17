package com.example.my_messenger

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class UserListFragment : Fragment() {

    private lateinit var viewModel: UserListViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_user_list, container, false)

        val searchInput = view.findViewById<EditText>(R.id.searchInput)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = UserAdapter { userId ->
            val action = MainFragmentDirections.actionMainFragmentToUserListFragment(userId)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
        val firebaseAuth = FirebaseAuth.getInstance()
        val factory = ViewModelFactory(firebaseAuth)
        viewModel = ViewModelProvider(this, factory)[UserListViewModel::class.java]

        viewModel.userList.observe(viewLifecycleOwner) { userList ->
            adapter.submitList(userList)
        }

        searchInput.addTextChangedListener { query ->
            viewModel.search(query.toString())
        }

        return view
    }
}