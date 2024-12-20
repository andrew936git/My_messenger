package com.example.my_messenger.users

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.my_messenger.databinding.FragmentAboutUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class AboutUserFragment : Fragment() {

    private var _binding: FragmentAboutUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var profile: User
    private val firestore = FirebaseFirestore.getInstance()
    private val avatarCollection = firestore.collection("avatar")
    private val avatarsList = mutableListOf<Avatar>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = arguments?.getString("userId")

        avatarCollection.orderBy("id", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(requireContext(), "Unable to get avatar", LENGTH_SHORT)
                        .show()
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {

                    for (document in snapshot.documents) {
                        val avatar = document.toObject(Avatar::class.java)
                        if (avatar != null) {
                            avatarsList.add(avatar)
                        }
                    }

                }
            }

        if (userId != null) {
            FirebaseFirestore.getInstance().collection("users")
                .get()
                .addOnSuccessListener { result ->
                    val userList: List<User> = result.documents.mapNotNull { document ->
                        document.toObject(User::class.java)
                    }
                    for (user in userList) {
                        if (userId == user.email) {
                            profile = user
                            if(FirebaseAuth.getInstance().currentUser?.uid == profile.id) {
                                if (User.myAvatar == "") continue
                                else {
                                    binding.circleImageView.setImageURI(Uri.parse(User.myAvatar))
                                }
                            }
                            else {
                                for (i in avatarsList) {
                                    if (i.id == profile.id) getAvatar(i.position)
                                }
                            }
                        }
                    }

                    binding.nameTV.text = "Имя: ${profile.name}"
                    binding.surnameTV.text = "Фамилия: ${profile.surname}"
                    binding.craftTV.text = "Род деятельности: ${profile.craft}"
                    binding.ageTV.text = "Возраст: ${profile.age}"
                    binding.cityTV.text = "Город: ${profile.city}"
                }
        }
    }


    private fun getAvatar(position: Int) {

        val photo = Avatar.avatars[position]
        val bitPhoto = getBitmapFromVectorDrawable(requireContext(), photo)
        binding.circleImageView.setImageBitmap(bitPhoto)
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