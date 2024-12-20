package com.example.my_messenger.chat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_messenger.R
import com.example.my_messenger.databinding.FragmentPersonalChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlin.random.Random


class PersonalChatFragment : Fragment() {
    private var _binding: FragmentPersonalChatBinding? = null
    private val binding get() = _binding!!
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
                            || message?.senderId == recipientName && message?.recipientId == senderName
                        ) {
                            messages.add((message ?: "") as Message)
                        }
                    }
                    messageAdapter.notifyDataSetChanged()
                    binding.recyclerView.scrollToPosition(messages.size - 1)
                }
            }

        messageAdapter = MessageAdapter(messages)
        binding.recyclerView.adapter = messageAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        messageAdapter.notifyDataSetChanged()

        binding.editTextMessage.setOnKeyListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                binding.buttonSend.performClick()
                true
            } else {
                false
            }
        }

        binding.buttonSend.setOnClickListener {
            val messageText = binding.editTextMessage.text.toString()
            sendMessage(messageText, senderName)

        }
        binding.emojiCV.setOnClickListener {

            val random = Random
            val message = Message(
                id = messagesCollection.document().id,
                message = "image${random.nextInt(0, 8)}",
                senderId = senderName!!,
                recipientId = recipientName!!,

                )
            messagesCollection.document(message.id).set(message)
        }

        messageAdapter.setOnMessageClickListener(object :
            MessageAdapter.OnMessageClickListener {
            override fun onMessageClick(message: Message, position: Int) {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle("Что вы хотите выполнить?")
                dialog.setPositiveButton("Удалить сообщение") { _, _ ->
                    FirebaseFirestore.getInstance().collection("messages").document(message.id)
                        .delete()
                    messages.removeAt(position)
                    messageAdapter.notifyDataSetChanged()

                }
                dialog.setNegativeButton("Отмена") { _, _ -> }
                dialog.create().show()
            }
        })
    }

    private fun sendMessage(messageText: String, senderName: String?) {
        if (messageText.isNotEmpty()) {
            val message = Message(
                id = messagesCollection.document().id,
                message = messageText,
                senderId = senderName!!,
                recipientId = recipientName!!,

                )
            messagesCollection.document(message.id).set(message)
            binding.editTextMessage.text.clear()
        }
    }

}
