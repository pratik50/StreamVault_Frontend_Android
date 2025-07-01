package com.pratik.streamvault.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.pratik.streamvault.R
import com.pratik.streamvault.data.local.UserPreferences
import com.pratik.streamvault.databinding.FragmentSignupBinding
import com.pratik.streamvault.presentation.auth.state.AuthState
import kotlin.getValue


class SignupFragment : Fragment() {


    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()
    private val userPreferences: UserPreferences by lazy {
        UserPreferences(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Idle -> {
                    // Handle idle state
                    binding.progressBar.isVisible = false
                }
                is AuthState.Loading -> {
                    // Handle loading state
                    binding.progressBar.isVisible = true
                }
                is AuthState.Success -> {
                    // Handle success state
                    userPreferences.saveToken(state.token)
                    binding.progressBar.isVisible = false
                    findNavController().navigate(R.id.action_signupFragment_to_dashboardFragment)

                    Toast.makeText(requireContext(), "SignUp successful", Toast.LENGTH_SHORT).show()
                }
                is AuthState.Error -> {
                    // Handle error state
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.alreadyHaveAcc.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        binding.btnSignup.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            viewModel.signup(email, password)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}