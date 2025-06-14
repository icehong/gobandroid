package org.ligi.gobandroid_hd.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.ligi.gobandroid_hd.R
import org.ligi.gobandroid_hd.databinding.GameExtraReviewBinding
import org.ligi.gobandroid_hd.events.GameChangedEvent
import org.ligi.gobandroid_hd.ui.go_terminology.GoTerminologyViewActivity

class CommentAndNowPlayingFragment : GobandroidGameAwareFragment() {
    private var _binding: GameExtraReviewBinding? = null
    private val binding get() = _binding!!

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = GameExtraReviewBinding.inflate(inflater, container, false)
        onGoGameChanged(null)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGoGameChanged(gameChangedEvent: GameChangedEvent?) {
        super.onGoGameChanged(gameChangedEvent)
        requireActivity().runOnUiThread {
            binding.includeComment.commentsTextview.let {
                it.text = game.actMove.comment
                GoTerminologyViewActivity.linkifyTextView(it)
            }
        }
    }

}
