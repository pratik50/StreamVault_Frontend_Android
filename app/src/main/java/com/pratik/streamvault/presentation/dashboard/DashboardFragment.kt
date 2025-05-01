package com.pratik.streamvault.presentation.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.pratik.streamvault.R
import com.pratik.streamvault.data.local.UserPreferences
import com.pratik.streamvault.databinding.FragmentDashboardBinding
import com.pratik.streamvault.model.FileItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: DashboardViewModel by viewModels()
    private val userPrefs: UserPreferences by lazy {
        UserPreferences(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val epoxyController = DashboardEpoxyController()
        binding.epoxyRecyclerView.setController(epoxyController)


        //For testing purpose (temporary)
        val sampleFiles = listOf(
            FileItem("1", "video1.mp4", "video/mp4", 5242880, "/uploads/video1.mp4"),
            FileItem("2", "img1.jpg", "image/jpeg", 102400, "/uploads/img1.jpg")
        )
        epoxyController.files = sampleFiles

        binding.welcomeText.text = "Welcome, ${userPrefs.getToken()}"

        binding.btnLogout.setOnClickListener {
            viewmodel.logout(userPrefs)
        }

        lifecycleScope.launchWhenStarted {
            viewmodel.navigateToLogin.collect {
                findNavController().navigate(
                    R.id.action_dashboardFragment_to_loginFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.nav_graph, true)
                        .build())
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}