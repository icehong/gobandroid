package org.ligi.gobandroid_hd.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.ligi.gobandroid_hd.R
import org.ligi.gobandroid_hd.databinding.GameExtraReviewBinding
import org.ligi.gobandroid_hd.events.GameChangedEvent
import org.ligi.gobandroid_hd.ui.go_terminology.GoTerminologyViewActivity

class NavigationAndCommentFragment : GobandroidGameAwareFragment() {
    private var _binding: GameExtraReviewBinding? = null
    private val binding get() = _binding!!

    private val gameChangeHandler by lazy { Handler() }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = GameExtraReviewBinding.inflate(inflater, container, false)

        binding.includeComment.commentsTextview.isFocusable = false
        binding.scrollview.isFocusable = false

        onGoGameChanged(null)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGoGameChanged(gameChangedEvent: GameChangedEvent?) {
        super.onGoGameChanged(gameChangedEvent)
        gameChangeHandler.post {
            binding.includeComment.commentsTextview.let {
                it.text = game.actMove.comment
                GoTerminologyViewActivity.linkifyTextView(it)
            }
        }
    }

}
