package org.ligi.gobandroid_hd.ui.sgf_listing.item_view_holder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import org.ligi.gobandroid_hd.FileEncodeDetector
import org.ligi.gobandroid_hd.databinding.SgfReviewGameDetailsListItemBinding
import org.ligi.gobandroid_hd.logic.MetaDataFormatter
import org.ligi.gobandroid_hd.logic.sgf.SGFReader
import org.ligi.gobandroid_hd.ui.review.SGFMetaData
import org.ligi.gobandroid_hd.ui.sgf_listing.GoLink
import java.io.File
import java.io.IOException

class ReviewViewHolder(_binding: SgfReviewGameDetailsListItemBinding) : RecyclerView.ViewHolder(_binding.root), ViewHolderInterface {
    private val binding = _binding

    override fun apply(fileToApply: File) {
        var file = fileToApply

        binding.title.text = file.name.replace(".sgf", "")

        try {
            if (GoLink.isGoLink(file)) {
                val gl = GoLink(file)
                file = File(gl.fileName)
                binding.gameLinkExtraInfos.text = "Move #" + gl.moveDepth
            } else {
                binding.gameLinkExtraInfos.visibility = View.GONE
            }

            val sgf_str = file.bufferedReader(FileEncodeDetector.detect(file)).readText()
            val game = SGFReader.sgf2game(sgf_str, null, SGFReader.BREAKON_FIRSTMOVE)
            val sgf_meta = SGFMetaData(file.absolutePath)

            if (game != null) {
                val metaFormatter = MetaDataFormatter(game)

                if (metaFormatter.getWhitePlayerString().isEmpty()) {
                    binding.playerWhiteStoneImg.visibility = View.GONE
                    binding.playerWhite.visibility = View.GONE
                } else {
                    binding.playerWhite.text = metaFormatter.getWhitePlayerString()
                }

                if (metaFormatter.getBlackPlayerString().isEmpty()) {
                    binding.playerBlackStoneImg.visibility = View.GONE
                    binding.playerBlack.visibility = View.GONE
                } else {
                    binding.playerBlack.text = metaFormatter.getBlackPlayerString()
                }

                binding.gameExtraInfos.text = metaFormatter.extrasString

                if (!sgf_meta.hasData()) {
                    binding.gameRating.visibility = View.GONE
                } else if (sgf_meta.rating != null) {
                    binding.gameRating.visibility = View.VISIBLE
                    binding.gameRating.rating = .5f * sgf_meta.rating!!
                }
            }

        } catch (e: IOException) {
        }

    }


}
