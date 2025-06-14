package org.ligi.gobandroid_hd.ui.links

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.ligi.gobandroid_hd.R
import org.ligi.gobandroid_hd.databinding.TwoLineListItemBinding

internal class TwoLineRecyclerAdapter(private val twoLinedWithLinkContent: Array<LinkWithDescription>) : RecyclerView.Adapter<TwoLineRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TwoLineRecyclerViewHolder {
        val inflatedView = TwoLineListItemBinding.inflate(LayoutInflater.from(parent.context))
        return TwoLineRecyclerViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: TwoLineRecyclerViewHolder, position: Int) {
        holder.bind(twoLinedWithLinkContent[position])
    }

    override fun getItemCount(): Int {
        return twoLinedWithLinkContent.size
    }
}
