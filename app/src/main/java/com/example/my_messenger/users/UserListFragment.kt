package com.example.my_messenger.users

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_messenger.R
import com.example.my_messenger.databinding.FragmentUserListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatAdapter: ChatAdapter
    private var firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        fetchUsers { users ->
            chatAdapter = ChatAdapter(users)
            binding.recyclerView.adapter = chatAdapter
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.setHasFixedSize(true)
            chatAdapter.setOnChatClickListener(object:
                ChatAdapter.OnChatClickListener{
                override fun onChatClick(chat: User, position: Int) {
                    val bundle = Bundle()
                    val chatName = users[position].email
                    bundle.putString("userId", chatName)
                    val action = R.id.action_userListFragment_to_aboutUserFragment
                    findNavController().navigate(action, bundle)

                }
            })
        }
    }

    private fun fetchUsers(callback: (List<User>) -> Unit) {
        firestore.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val userList: List<User> = result.documents.mapNotNull { document ->
                    document.toObject(User::class.java)
                }
                val users = mutableListOf<User>()
                for (user in userList){
                    if (user.email == FirebaseAuth.getInstance().currentUser?.email){
                        user.displayName = "Мой профиль"
                        users.add(user)
                    }
                    else users.add(user)
                }

                callback(users)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching users", e)
                callback(emptyList())
            }
    }
}