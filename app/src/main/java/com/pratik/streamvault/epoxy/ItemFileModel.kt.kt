package com.pratik.streamvault.epoxy

import android.view.View
import android.view.ViewParent
import android.widget.TextView
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.pratik.streamvault.R

class ItemFileModel(
    val fileNameValue: String,
    val fileSizeValue: String
) : EpoxyModelWithHolder<ItemFileModel.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.item_file

    override fun bind(holder: Holder) {
        holder.fileName.text = fileNameValue
        holder.fileSize.text = fileSizeValue
    }

    class Holder : EpoxyHolder() {
        lateinit var fileName: TextView
        lateinit var fileSize: TextView

        override fun bindView(itemView: View) {
            fileName = itemView.findViewById(R.id.fileName)
            fileSize = itemView.findViewById(R.id.fileSize)
        }
    }

    override fun createNewHolder(parent: ViewParent): Holder? {
        return Holder()
    }
}