package com.example.my_messenger.password_recovery

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.my_messenger.ViewModelFactory
import com.example.my_messenger.databinding.FragmentForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val firebaseAuth = FirebaseAuth.getInstance()
        val factory = ViewModelFactory(firebaseAuth)
        viewModel = ViewModelProvider(this, factory)[ForgotPasswordViewModel::class.java]

        val emailInput = binding.etEmail
        val sendEmailButton = binding.btnSendResetEmail

        sendEmailButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            sendEmailButton.isEnabled = false
            viewModel.sendResetEmail(email)
        }

        viewModel.isSendingEmail.observe(viewLifecycleOwner) { isSending ->
            sendEmailButton.isEnabled = !isSending
            sendEmailButton.text = "Отправить письмо"
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isEmailSent.observe(viewLifecycleOwner) { isSent ->
            if (isSent) {
                Toast.makeText(context, "Письмо отправлено", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

    }
}