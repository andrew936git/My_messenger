package com.example.my_messenger

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.my_messenger.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firebaseAuth = FirebaseAuth.getInstance()
        val factory = ViewModelFactory(firebaseAuth)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        val emailInput = binding.etEmail
        val passwordInput = binding.etPassword
        val loginButton = binding.btnLogin
        val registerTV = binding.tvRegister
        val forgotPasswordTV = binding.tvForgotPassword

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            loginButton.isEnabled = false
            loginButton.text = "Загрузка..."
            viewModel.login(email, password)
        }

        registerTV.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        forgotPasswordTV.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        viewModel.isLoggingIn.observe(viewLifecycleOwner) { isLoggingIn ->
            loginButton.isEnabled = !isLoggingIn
            loginButton.text = if (isLoggingIn) "Загрузка..." else "Войти"
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            if (isLoggedIn) {
                findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            }
        }

    }
}