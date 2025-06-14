package org.ligi.gobandroid_hd.ui.sgf_listing.item_view_holder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import org.ligi.gobandroid_hd.databinding.SgfDirListItemBinding
import java.io.File

class PathViewHolder(_binding: SgfDirListItemBinding) : RecyclerView.ViewHolder(_binding.root), ViewHolderInterface {
    private val binding = _binding

    override fun apply(file: File) {
        binding.pathName.text = file.name
    }
}
