package com.example.my_messenger.users

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.my_messenger.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment(){

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val firestore = FirebaseFirestore.getInstance().collection("users")
    private val userRef = firestore.document(FirebaseAuth.getInstance().currentUser?.uid!!)
    val GALLERY_REQUEST = 302
    private var editPhotoUri: Uri? = null
    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editImageProductIV.setOnClickListener{
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
            binding.nameET.text.clear()
            binding.surnameET.text.clear()
            binding.craftET.text.clear()
            binding.ageET.text.clear()
            binding.cityET.text.clear()

                }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        when(requestCode){
            GALLERY_REQUEST -> {
                if (resultCode === RESULT_OK){
                    editPhotoUri = data?.data
                    binding.editImageProductIV.setImageURI(editPhotoUri)
                    viewModel.stringData.value = editPhotoUri.toString()
                }
            }
        }
    }

}