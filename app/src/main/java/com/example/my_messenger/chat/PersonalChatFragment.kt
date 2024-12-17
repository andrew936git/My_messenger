
package com.example.my_messenger.chat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_messenger.R
import com.example.my_messenger.databinding.FragmentPersonalChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.io.InputStreamReader
import java.lang.reflect.Field


class PersonalChatFragment : Fragment() {
    private var _binding: FragmentPersonalChatBinding? = null
    private val binding get() = _binding!!
    private var senderName: String? = null
    private var recipientName: String? = null
    private lateinit var messageAdapter: MessageAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private val messagesCollection = firestore.collection("messages")
    private val messages = mutableListOf<Message>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text = arguments?.getString("chatId")
        if (text != null) {
            recipientName = text
        }
        val senderName = FirebaseAuth.getInstance().currentUser?.email

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
                        if (message?.senderId == senderName && message?.recipientId == recipientName
                            || message?.senderId == recipientName && message?.recipientId == senderName) {
                            messages.add((message ?: "") as Message)
                        }
                    }
                    messageAdapter.notifyDataSetChanged()
                    binding.recyclerView.scrollToPosition(messages.size - 1)
                }
            }



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
                    findNavController().navigate(R.id.action_personalChatFragment_to_profileFragment)
                    true
                }
            }
            setTitle("Сообщения")
        }

        messageAdapter = MessageAdapter(messages)
        binding.recyclerView.adapter = messageAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        messageAdapter.notifyDataSetChanged()


        binding.buttonSend.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString()
            if (messageText.isNotEmpty()) {
                val message = Message(
                    id = messagesCollection.document().id,
                    message = messageText,
                    senderId = senderName!!,
                    recipientId = recipientName!!
                )
                messagesCollection.document(message.id).set(message)
                binding.editTextMessage.text.clear()
            }

        }

        messageAdapter.setOnMessageClickListener(object:
            MessageAdapter.OnMessageClickListener{
            override fun onMessageClick(message: Message,position: Int) {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle("Что вы хотите выполнить?")
                dialog.setPositiveButton("Удалить сообщение"){_, _->
                    FirebaseFirestore.getInstance().collection("messages").document(message.id).delete()
                    messages.removeAt(position)
                    messageAdapter.notifyDataSetChanged()

                }
                dialog.setNegativeButton("Отмена"){_,_->}
                dialog.create().show()
                false
            }

        })

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
                        if (message?.senderId == senderName && message?.recipientId == recipientName
                            || message?.senderId == recipientName && message?.recipientId == senderName) {
                            messages.add((message ?: "") as Message)
                        }
                    }
                    messageAdapter.notifyDataSetChanged()
                    binding.recyclerView.scrollToPosition(messages.size - 1)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Unable to fetch Messages", LENGTH_SHORT).show()
            }
    }

}
