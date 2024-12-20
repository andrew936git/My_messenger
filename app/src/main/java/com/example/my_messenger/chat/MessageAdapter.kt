package com.example.my_messenger.chat

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.my_messenger.R
import de.hdodenhof.circleimageview.CircleImageView

class MessageAdapter(
    private val messages: MutableList<Message>): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var onMessageClickListener: OnMessageClickListener? = null
    private lateinit var context: Context


    interface OnMessageClickListener {
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
        val message = messages[position]
        when(message.message){
            "image0" -> {
                holder.messageCV.setImageBitmap(
                    getBitmapFromVectorDrawable(
                        context,
                        Message.emoji[0]
                    )
                )
                holder.messageCV.setWillNotDraw(false)
                holder.text.text = ""
            }
            "image1" -> {
                holder.messageCV.setImageBitmap(
                    getBitmapFromVectorDrawable(
                        context,
                        Message.emoji[1]
                    )
                )
                holder.messageCV.setWillNotDraw(false)
                holder.text.text = ""
            }
            "image2" -> {
                holder.messageCV.setImageBitmap(
                    getBitmapFromVectorDrawable(
                        context,
                        Message.emoji[2]
                    )
                )
                holder.messageCV.setWillNotDraw(false)
                holder.text.text = ""
            }
            "image3" -> {
                holder.messageCV.setImageBitmap(
                    getBitmapFromVectorDrawable(
                        context,
                        Message.emoji[3]
                    )
                )
                holder.messageCV.setWillNotDraw(false)
                holder.text.text = ""
            }
            "image4" -> {
                holder.messageCV.setImageBitmap(
                    getBitmapFromVectorDrawable(
                        context,
                        Message.emoji[4]
                    )
                )
                holder.messageCV.setWillNotDraw(false)
                holder.text.text = ""
            }
            "image5" -> {
                holder.messageCV.setImageBitmap(
                    getBitmapFromVectorDrawable(
                        context,
                        Message.emoji[5]
                    )
                )
                holder.messageCV.setWillNotDraw(false)
                holder.text.text = ""
            }
            "image6" -> {
                holder.messageCV.setImageBitmap(
                    getBitmapFromVectorDrawable(
                        context,
                        Message.emoji[6]
                    )
                )
                holder.messageCV.setWillNotDraw(false)
                holder.text.text = ""
            }
            "image7" -> {
                holder.messageCV.setImageBitmap(
                    getBitmapFromVectorDrawable(
                        context,
                        Message.emoji[7]
                    )
                )
                holder.messageCV.setWillNotDraw(false)
                holder.text.text = ""
            }

            else -> {
                holder.text.text = message.message
                holder.messageCV.setWillNotDraw(true)
            }
        }
        holder.userName.text = message.senderId
        holder.itemView.setOnClickListener {
            if (onMessageClickListener != null) {
                onMessageClickListener!!.onMessageClick(message, position)
            }
        }
    }


    override fun getItemCount(): Int {
        return messages.size
    }

    fun setOnMessageClickListener(onMessageClickListener: OnMessageClickListener) {
        this.onMessageClickListener = onMessageClickListener
    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}