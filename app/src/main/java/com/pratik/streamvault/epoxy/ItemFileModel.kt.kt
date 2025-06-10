package com.pratik.streamvault.epoxy

import android.view.View
import android.view.ViewParent
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.pratik.streamvault.R
import com.pratik.streamvault.presentation.dashboard.state.FileItemClickListener

class ItemFileModel(
    val fileNameValue: String,
    val fileSizeValue: String,
    val fileId: String,
    val clickListener: FileItemClickListener?
) : EpoxyModelWithHolder<ItemFileModel.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.item_file

    override fun bind(holder: Holder) {
        holder.fileName.text = fileNameValue
        holder.fileSize.text = fileSizeValue

        holder.menuIcon.setOnClickListener {

            val popup = PopupMenu(holder.menuIcon.context, holder.menuIcon)
            popup.inflate(R.menu.recycler_item_menu)

            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_delete -> {
                        clickListener?.onDelete(fileId)
                        true
                    }
                    R.id.action_download -> {
                        clickListener?.onDownload(fileId)
                        true
                    }
                    R.id.action_share -> {
                        clickListener?.onShare(fileId)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    class Holder : EpoxyHolder() {
        lateinit var fileName: TextView
        lateinit var fileSize: TextView
        lateinit var menuIcon: ImageView
        lateinit var fileIcon: ImageView

        override fun bindView(itemView: View) {
            fileName = itemView.findViewById(R.id.fileName)
            fileSize = itemView.findViewById(R.id.fileSize)
            fileIcon = itemView.findViewById(R.id.fileType_icon)
            menuIcon = itemView.findViewById(R.id.item_menu_icon)
        }
    }

    override fun createNewHolder(parent: ViewParent): Holder? {
        return Holder()
    }
}