package com.example.my_messenger.registration

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.my_messenger.R
import com.example.my_messenger.chat.User
import com.example.my_messenger.databinding.FragmentAboutUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class AboutUserFragment : Fragment() {

    private var _binding: FragmentAboutUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var profile: User

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

}