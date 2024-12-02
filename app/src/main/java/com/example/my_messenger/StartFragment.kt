package com.example.my_messenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dotlottie.dlplayer.Mode
import com.example.my_messenger.databinding.FragmentStartBinding
import com.google.firebase.auth.FirebaseAuth
import com.lottiefiles.dotlottie.core.model.Config
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStreamReader


class StartFragment : Fragment() {
    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            playAnim()
        }

        lifecycleScope.launch {
            delay(5000L)
            transition()
        }
    }

    private fun playAnim(){
        val dotLottieAnimationView = binding.lottieView
        val config = Config.Builder()
            .autoplay(true)
            .speed(1f)
            .loop(true)
            .source(DotLottieSource.Url("https://lottie.host/15dc9074-5a0c-498f-87c9-c3a6c250e669/uH9ocaGJXO.lottie"))
            .playMode(Mode.FORWARD)
            .build()
        dotLottieAnimationView.load(config)
    }

    private fun transition(){

        findNavController().navigate(R.id.action_startFragment_to_loginFragment)
    }




}