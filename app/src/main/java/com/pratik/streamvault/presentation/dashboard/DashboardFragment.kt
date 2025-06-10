package com.pratik.streamvault.presentation.dashboard

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pratik.streamvault.R
import com.pratik.streamvault.data.local.UserPreferences
import com.pratik.streamvault.databinding.FragmentDashboardBinding
import com.pratik.streamvault.presentation.dashboard.state.FileItemClickListener
import com.pratik.streamvault.presentation.dashboard.state.UploadState
import com.pratik.streamvault.utils.CameraUtils
import com.pratik.streamvault.utils.FileFolderPickerUtils
import com.pratik.streamvault.utils.ShareUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private lateinit var cameraUri: Uri
    private lateinit var cameraFile: File

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: DashboardViewModel by viewModels()
    private val userPrefs: UserPreferences by lazy {
        UserPreferences(requireContext())
    }
    private val shareUtils = ShareUtils()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Epoxy Initialization
        val epoxyController = DashboardEpoxyController()
        binding.epoxyRecyclerView.setController(epoxyController)

        viewmodel.loadFiles()
        viewmodel.files.observe(viewLifecycleOwner) { response ->
            epoxyController.files = response.unfolderedFiles
            binding.swipeRefreshLayout.isRefreshing = false
        }

        // Observe upload state for UI feedback
        viewmodel.uploadState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UploadState.Loading -> {
                    binding.btnUpload.isEnabled = false
                }

                is UploadState.Success -> {
                    binding.btnUpload.isEnabled = true

                    val updatedList = epoxyController.files.toMutableList()
                    updatedList.add(0, state.uploadFileResponse.file)
                    epoxyController.files = updatedList

                    binding.epoxyRecyclerView.scrollToPosition(0)
                    Toast.makeText(
                        requireContext(),
                        "File uploaded successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is UploadState.Error -> {
                    binding.btnUpload.isEnabled = true
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.apply {

            // Camera button listener
            btnScan.setOnClickListener {
                handleCameraPermission()
            }

            // Upload button listener
            btnUpload.setOnClickListener {
                showActionBottomSheet()
            }

            // Open Navigation drawer
            searchBar.setNavigationOnClickListener{ view->
                drawerLayout.open()
            }

            // Swipe to refresh listener
            swipeRefreshLayout.setOnRefreshListener {
                viewmodel.loadFiles()
            }

            // Navigation drawer item listener
            navigationView.setNavigationItemSelectedListener { menuItem->

                menuItem.isChecked = true

                when (menuItem.itemId){
                    R.id.action_recent -> {
                        drawerLayout.close()
                        Toast.makeText(requireContext(), "Recent", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_offline -> {
                        drawerLayout.close()
                        Toast.makeText(requireContext(), "Offline", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_trash -> {
                        drawerLayout.close()
                        Toast.makeText(requireContext(), "Bin", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_settings -> {
                        drawerLayout.close()
                        Toast.makeText(requireContext(), "Settings", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_help -> {
                        drawerLayout.close()
                        Toast.makeText(requireContext(), "Help", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_logout -> {
                        drawerLayout.close()
                        viewmodel.logout(userPrefs)

                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.dashboardFragment, true)
                            .build()
                        findNavController().navigate(R.id.action_dashboardFragment_to_loginFragment, null, navOptions)

                        Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
                    }
                }

                menuItem.isChecked = false
                return@setNavigationItemSelectedListener true
            }


        }

        //File menu listener & controller
        epoxyController.clickListener = object : FileItemClickListener {
            override fun onDelete(fileId: String) {
                viewmodel.deleteFile(fileId)
            }

            override fun onDownload(fileId: String) {
                //viewmodel.downloadFile(fileId::fileUrl, fileId::fileName, requireContext())
            }

            override fun onShare(fileId: String) {
                viewmodel.generateShareLink(fileId)
            }
        }

        // Delete file Listener
        viewmodel.deleteState.observe(viewLifecycleOwner) { deleted ->
            if (deleted) {
                Toast.makeText(requireContext(), "File deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to delete file", Toast.LENGTH_SHORT).show()
            }
        }

        // Share link listener
        viewmodel.shareState.observe(viewLifecycleOwner) { link ->
            if (!link.isNullOrBlank()) {
                shareUtils.shareTextLink(requireContext(), link)
                viewmodel.clearShareState()
            }
        }

    }

    private fun showActionBottomSheet() {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialog.setContentView(R.layout.bottom_sheet_upload)
        dialog.show()

        val cameraButton = dialog.findViewById<LinearLayout>(R.id.btnCamera)
        val uploadButton = dialog.findViewById<LinearLayout>(R.id.btnUpload)
        val folderButton = dialog.findViewById<LinearLayout>(R.id.btnFolder)

        cameraButton?.setOnClickListener {
            handleCameraPermission()
            dialog.dismiss()
        }

        uploadButton?.setOnClickListener {
            val intent = FileFolderPickerUtils.getImagePickerIntent()
            filePickerLauncher.launch(intent.type)
            dialog.dismiss()
        }

        folderButton?.setOnClickListener {
            // Handle folder button click
            findNavController().navigate(R.id.action_dashboardFragment_to_signupFragment)
            Toast.makeText(requireContext(), "Folder button clicked", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

    }

    // FileManager launcher
    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            viewmodel.uploadFile(it)
        }
    }

    // Handle camera permission
    private fun handleCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCameraFlow()
        } else {
            cameraPermissionRequestLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Camera permission launcher
    private val cameraPermissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCameraFlow()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Go to settings and enable camera permission to use this feature",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    // Launch camera when permission is granted
    private fun startCameraFlow() {
        val cameraData = CameraUtils.prepareCameraData(requireContext())
        if (cameraData != null) {
            cameraUri = cameraData.uri
            cameraFile = cameraData.file
            takePictureLauncher.launch(cameraData.intent)
        } else {
            Toast.makeText(requireContext(), "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    // Capture and upload image
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && ::cameraUri.isInitialized) {
                // Verify file exists and is not empty
                if (cameraFile.exists() && cameraFile.length() > 0) {
                    viewmodel.uploadFile(cameraUri)
                } else {
                    // Clean up empty file
                    cameraFile.delete()
                    Toast.makeText(
                        requireContext(),
                        "No image captured",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                // Clean up file on cancel
                if (::cameraFile.isInitialized) {
                    cameraFile.delete()
                }
            }
        }


    // Detaching the binding to avoid memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}