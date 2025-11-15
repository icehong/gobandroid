package org.ligi.gobandroid_hd.ui.links

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import org.ligi.gobandroid_hd.databinding.TwoLineListItemBinding

class TwoLineRecyclerViewHolder(_binding: TwoLineListItemBinding) : RecyclerView.ViewHolder(_binding.root) {
    private val binding = _binding

    fun bind(twoLinedWithLink: LinkWithDescription) {
        binding.text1.text = twoLinedWithLink.line1
        binding.text2.text = twoLinedWithLink.line2
        itemView.setOnClickListener { v -> v.context.startActivity(Intent("android.intent.action.VIEW", Uri.parse(twoLinedWithLink.link))) }
    }
}
