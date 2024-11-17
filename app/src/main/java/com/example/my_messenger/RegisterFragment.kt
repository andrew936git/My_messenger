package com.example.my_messenger

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.my_messenger.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegisterViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firebaseAuth = FirebaseAuth.getInstance()
        val factory = ViewModelFactory(firebaseAuth)
        viewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        val emailInput = binding.etRegisterEmail
        val passwordInput = binding.etRegisterPassword
        val registerButton = binding.btnRegister


        registerButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            registerButton.isEnabled = false
            registerButton.text = "Загрузка..."

            viewModel.register(email, password)
        }

        viewModel.isRegistering.observe(viewLifecycleOwner) { isRegistering ->
            registerButton.isEnabled = !isRegistering
            registerButton.text = if (isRegistering) "Загрузка..." else "Зарегистрироваться"
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isRegistered.observe(viewLifecycleOwner) { isRegistered ->
            if (isRegistered) {
                Toast.makeText(context, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }

    }
}

