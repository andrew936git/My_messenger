package com.example.my_messenger.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.my_messenger.R

class MessageAdapter(private val messages: MutableList<Message>): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {



    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.tvChatName)
        val text: TextView = itemView.findViewById(R.id.tvLastMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.userName.text = message.senderId
        holder.text.text = message.message
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}