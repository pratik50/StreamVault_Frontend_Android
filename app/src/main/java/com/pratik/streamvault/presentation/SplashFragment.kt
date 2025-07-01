package com.pratik.streamvault.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pratik.streamvault.R
import com.pratik.streamvault.data.local.UserPreferences
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private val userPrefs: UserPreferences by lazy {
        UserPreferences(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {

            val token = userPrefs.getToken()

            Handler(Looper.getMainLooper()).postDelayed({
                if (token.isNullOrEmpty()) {
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_dashboardFragment)
                }
            }, 2000)
        }
    }
}