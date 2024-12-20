package com.example.my_messenger.users

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.my_messenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(private val users: List<User>): RecyclerView.Adapter<ChatAdapter.UserViewHolder>() {

    private var onChatClickListener: OnChatClickListener? = null
    private lateinit var context: Context
    private val firestore = FirebaseFirestore.getInstance()
    private val avatarCollection = firestore.collection("avatar")
    private lateinit var userAvatar: Avatar

    interface OnChatClickListener{
        fun onChatClick(chat: User, position: Int)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.tvChatName)
        val avatar: CircleImageView = itemView.findViewById(R.id.avatarCV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        context = parent.context
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val user = users[position]
        if (user.id == FirebaseAuth.getInstance().currentUser?.uid){
            holder.avatar.setImageURI(Uri.parse(User.myAvatar))
        }
        else {
            avatarCollection.orderBy("id", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Toast.makeText(context, "Unable to get avatar", LENGTH_SHORT)
                            .show()
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {

                        for (document in snapshot.documents) {
                            val avatar = document.toObject(Avatar::class.java)
                            if (avatar?.id == user.id) {
                                userAvatar = avatar
                                holder.avatar.setImageBitmap(getAvatar(userAvatar.position))
                            }
                        }

                    }
                }
        }
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

    private fun getAvatar(position: Int): Bitmap {

        val photo = Avatar.avatars[position]
        val bitPhoto = getBitmapFromVectorDrawable(context, photo)
        return bitPhoto
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