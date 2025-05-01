package com.pratik.streamvault.presentation.dashboard

import com.airbnb.epoxy.EpoxyController
import com.pratik.streamvault.epoxy.ItemFileModel
import com.pratik.streamvault.model.FileItem

class DashboardEpoxyController : EpoxyController() {

    var files: List<FileItem> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        files.forEach { file ->
            ItemFileModel(
                fileNameValue = file.name,
                fileSizeValue = "${file.size / 1024} KB"
            ).id(file.url).addTo(this)
        }
    }
}