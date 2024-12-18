package com.example.my_messenger.chat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_messenger.R
import com.example.my_messenger.databinding.FragmentChatListBinding
import com.example.my_messenger.users.User
import com.google.firebase.firestore.FirebaseFirestore

class ChatListFragment : Fragment() {

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatAdapter: ChatAdapter
    private var firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.apply {
            inflateMenu(R.menu.main_menu)
            menu.apply {
                findItem(R.id.profile).isVisible = true
                findItem(R.id.about).isVisible = false
                findItem(R.id.exit).isVisible = true
                findItem(R.id.exit).setOnMenuItemClickListener {
                    val fileOutputStream = activity?.openFileOutput("loginpasswordfile.txt", Context.MODE_PRIVATE)
                    fileOutputStream?.close()
                    findNavController().navigate(R.id.action_chatListFragment_to_loginFragment)
                    true
                }

                findItem(R.id.profile).setOnMenuItemClickListener {
                    findNavController().navigate(R.id.action_chatListFragment_to_profileFragment)
                    true
                }
            }

            setTitle("Список пользователей")
        }

        fetchUsers { users ->
            chatAdapter = ChatAdapter(users)
            binding.recyclerView.adapter = chatAdapter
            binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerView.setHasFixedSize(true)
            chatAdapter.setOnChatClickListener(object:
                ChatAdapter.OnChatClickListener{
                override fun onChatClick(chat: User, position: Int) {
                    val dialog = AlertDialog.Builder(requireContext())
                    dialog.setTitle("Что вы хотите выполнить?")
                    dialog.setPositiveButton("Перейти к сообщениям"){_, _->
                        val bundle = Bundle()
                        val chatName = users[position].displayName
                        bundle.putString("chatId", chatName)
                        val action = R.id.action_chatListFragment_to_personalChatFragment
                        findNavController().navigate(action, bundle)

                    }
                    dialog.setNegativeButton("Перейти к профилю"){_,_->
                        val bundle = Bundle()
                        val chatName = users[position].displayName
                        bundle.putString("userId", chatName)
                        val action = R.id.action_chatListFragment_to_aboutUserFragment
                        findNavController().navigate(action, bundle)
                    }
                    dialog.create().show()

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

                callback(userList)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching users", e)
                callback(emptyList())
            }
    }
}






