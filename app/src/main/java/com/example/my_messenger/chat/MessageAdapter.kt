package com.example.my_messenger.chat

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.my_messenger.R
import com.example.my_messenger.users.Avatar
import com.example.my_messenger.users.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.random.Random
import kotlin.random.nextInt

class MessageAdapter(private val messages: MutableList<Message>): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var onMessageClickListener: OnMessageClickListener? = null
    private lateinit var context: Context

    interface OnMessageClickListener{
        fun onMessageClick(message: Message, position: Int)
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.tvChatName)
        val text: TextView = itemView.findViewById(R.id.tvLastMessage)
        var messageCV: CircleImageView = itemView.findViewById(R.id.messageCV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        context = parent.context
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val random = Random
        val message = messages[position]
        if (message.message == "image") {
            val emoji = Message.emoji[random.nextInt(0, 9)]
            holder.messageCV.setImageBitmap(getBitmapFromVectorDrawable(context, emoji))
            message.message = ""
            holder.text.text = (message.message)
        }

        holder.text.text = message.message

        holder.userName.text = message.senderId

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

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

}