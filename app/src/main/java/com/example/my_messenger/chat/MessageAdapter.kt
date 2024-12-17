package com.example.my_messenger.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.my_messenger.R

class MessageAdapter(private val messages: MutableList<Message>): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var onMessageClickListener: OnMessageClickListener? = null

    interface OnMessageClickListener{
        fun onMessageClick(message: Message, position: Int)
    }

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
        holder.itemView.setOnClickListener{
            if (onMessageClickListener != null){
                onMessageClickListener!!.onMessageClick(message, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun setOnMessageClickListener(onMessageClickListener: OnMessageClickListener){
        this.onMessageClickListener = onMessageClickListener
    }
}