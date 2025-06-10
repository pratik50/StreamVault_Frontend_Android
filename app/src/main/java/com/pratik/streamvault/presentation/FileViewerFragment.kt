package com.pratik.streamvault.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.pratik.streamvault.databinding.FragmentFileViewerBinding
import com.pratik.streamvault.utils.NetworkConstants
import timber.log.Timber

class FileViewerFragment : Fragment() {

    private lateinit var binding: FragmentFileViewerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFileViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val fileUrl = arguments?.getString("fileUrl")!!
        val mimeType = arguments?.getString("mimeType")!!

        val fullUrl = "${NetworkConstants.BASE_URL}$fileUrl"

        if (mimeType.startsWith("image/")) {
            binding.imageView.visibility = View.VISIBLE
            Glide.with(this).load(fullUrl).into(binding.imageView)
        } else if (mimeType == "application/pdf") {
            binding.pdfView.visibility = View.VISIBLE
            val googleDocsUrl = "https://docs.google.com/gview?embedded=true&url=$fullUrl"
            Timber.d("Google Docs URL: $googleDocsUrl")
            binding.pdfView.settings.javaScriptEnabled = true
            binding.pdfView.loadUrl(googleDocsUrl)
        }
    }
}