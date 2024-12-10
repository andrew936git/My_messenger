package com.example.my_messenger.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.my_messenger.R

class ChatAdapter(private val users: List<User>): RecyclerView.Adapter<ChatAdapter.UserViewHolder>() {

    private var onChatClickListener: OnChatClickListener? = null

    interface OnChatClickListener{
        fun onChatClick(chat: User, position: Int)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.tvChatName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.userName.text = user.displayName
        holder.itemView.setOnClickListener{
            if (onChatClickListener != null){
                onChatClickListener!!.onChatClick(user, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun setOnChatClickListener(onChatClickListener: OnChatClickListener){
        this.onChatClickListener = onChatClickListener
    }
}