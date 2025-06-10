package com.pratik.streamvault.presentation.dashboard

import com.airbnb.epoxy.EpoxyController
import com.pratik.streamvault.epoxy.ItemFileModel
import com.pratik.streamvault.model.FileItem
import com.pratik.streamvault.presentation.dashboard.state.FileItemClickListener

class DashboardEpoxyController : EpoxyController() {

    var clickListener: FileItemClickListener? = null

    var files: List<FileItem> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        files.forEach { file ->
            ItemFileModel(
                fileNameValue = file.name,
                fileSizeValue = if (file.size < 1024 * 1024) {
                    "${file.size / 1024} KB"
                } else {
                    "%.2f MB".format(file.size / 1024f / 1024f)
                },
                fileUrl = file.url,
                fileType = file.type,
                fileId = file.id,
                clickListener = clickListener
            ).id(file.url).addTo(this)
        }
    }
}