package com.example.my_messenger.users

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.my_messenger.R
import com.example.my_messenger.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment(){

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val firestore = FirebaseFirestore.getInstance().collection("users")
    private val userRef = firestore.document(FirebaseAuth.getInstance().currentUser?.uid!!)
    private lateinit var profile: User
    private val GALLERY_REQUEST = 302
    private var editPhotoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseFirestore.getInstance().collection("users")
            .get()
            .addOnSuccessListener { result ->
                val userList: List<User> = result.documents.mapNotNull { document ->
                    document.toObject(User::class.java)
                }
                for (user in userList) {
                    if (FirebaseAuth.getInstance().currentUser?.email == user.email) {
                        profile = user
                        binding.nameET.setText(profile.name)
                        binding.surnameET.setText(profile.surname)
                        binding.craftET.setText(profile.craft)
                        binding.ageET.setText(profile.age)
                        binding.cityET.setText(profile.city)
                        if (User.myAvatar == "")continue
                        else{
                            binding.editImageProfileIV.setImageURI(Uri.parse(User.myAvatar))
                        }


                    }
                }
            }

        binding.editImageProfileIV.setOnClickListener{
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
        }



        binding.saveBT.setOnClickListener {

            val newName = binding.nameET.text.toString()
            val newSurname = binding.surnameET.text.toString()
            val newCraft = binding.craftET.text.toString()
            val newAge = binding.ageET.text.toString()
            val newCity = binding.cityET.text.toString()


            val updates = hashMapOf(
                "age" to newAge,
                "name" to newName,
                "craft" to newCraft,
                "surname" to newSurname,
                "city" to newCity,
            )

            userRef.update(updates as Map<String, String>).addOnSuccessListener {
                Log.d("Firestore Update", "DocumentSnapshot successfully updated!")
            }
            val action = R.id.action_profileFragment_to_mainFragment
            findNavController().navigate(action)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            GALLERY_REQUEST -> {
                if (resultCode === RESULT_OK){
                    editPhotoUri = data?.data
                    binding.editImageProfileIV.setImageURI(editPhotoUri)
                    User.myAvatar = editPhotoUri.toString()
                }
            }
        }
    }
}