package com.example.my_messenger.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.my_messenger.R
import com.example.my_messenger.ViewModelFactory
import com.example.my_messenger.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val firebaseAuth = FirebaseAuth.getInstance()
        val factory = ViewModelFactory(firebaseAuth)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

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
            viewModel.login(email, password)
            try {
                val fileOutputStream = activity?.openFileOutput("loginpasswordfile.txt", Context.MODE_PRIVATE)
                val outputWriter = OutputStreamWriter(fileOutputStream)
                outputWriter.write("$email/")
                outputWriter.write(password)
                outputWriter.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        registerTV.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        forgotPasswordTV.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            if (isLoggedIn) {
                findNavController().navigate(R.id.action_loginFragment_to_chatListFragment)
            }
        }

    }
}