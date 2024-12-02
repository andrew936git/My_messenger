package com.example.my_messenger.chat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_messenger.R
import com.example.my_messenger.databinding.FragmentChatListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChatListFragment : Fragment() {

    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatAdapter: ChatAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val messagesCollection = firestore.collection("messages")
    private val messages = mutableListOf<Message>()
    private lateinit var userName: String

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
            setTitle("Чат")
            setNavigationIcon(R.drawable.ic_menu)
            //выход из профиля
            setNavigationOnClickListener {
                val fileOutputStream = activity?.openFileOutput("loginpasswordfile.txt", Context.MODE_PRIVATE)
                fileOutputStream?.close()
                findNavController().navigate(R.id.action_chatListFragment_to_loginFragment)
            }
        }

        val users = FirebaseAuth.getInstance().currentUser?.email?.split("@")
        userName = users?.get(0) ?: ""
        chatAdapter = ChatAdapter(messages)
        binding.recyclerView.adapter = chatAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        fetchExistingMessages()

        binding.buttonSend.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString()
            if (messageText.isNotEmpty()) {
                val message = Message(
                    id = messagesCollection.document().id,
                    message = messageText,
                    senderId = userName.toString()
                )
                messagesCollection.document(message.id).set(message)
                binding.editTextMessage.text.clear()
            }

        }

        messagesCollection.orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(requireContext(), "Unable to get new Messages", LENGTH_SHORT)
                        .show()
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    messages.clear()
                    for (document in snapshot.documents) {
                        val message = document.toObject(Message::class.java)
                        if (message != null) {
                            messages.add(message)
                        }
                    }
                    chatAdapter.notifyDataSetChanged()
                    binding.recyclerView.scrollToPosition(messages.size - 1)
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchExistingMessages() {
        messagesCollection.orderBy("timestamp", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null && !snapshot.isEmpty) {
                    messages.clear()
                    for (document in snapshot.documents) {
                        val message = document.toObject(Message::class.java)
                        if (message != null) {
                            messages.add(message)
                        }
                    }
                    chatAdapter.notifyDataSetChanged()
                    binding.recyclerView.scrollToPosition(messages.size - 1)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Unable to fetch Messages", LENGTH_SHORT).show()
            }
    }
}


