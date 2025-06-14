package org.ligi.gobandroid_hd.ui.fragments

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import org.ligi.gobandroid_hd.R
import org.ligi.gobandroid_hd.databinding.NavButtonContainerBinding
import org.ligi.gobandroid_hd.events.GameChangedEvent
import org.ligi.gobandroid_hd.logic.GoMove
import org.ligi.gobandroid_hd.ui.GoPrefs
import org.ligi.gobandroid_hd.ui.alerts.GameForwardAlert

class NavigationFragment : GobandroidGameAwareFragment() {
    private var _binding: NavButtonContainerBinding? = null
    private val binding get() = _binding!!

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = NavButtonContainerBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        updateButtonStates()

        binding.btnNext.setOnClickListener {
            if (GoPrefs.isShowForwardAlertWanted) {
                GameForwardAlert.showIfNeeded(requireActivity(), game)
            } else {
                game.redo(0)
            }
        }

        binding.btnPrev.setOnClickListener {
            if (game.canUndo()) {
                game.undo()
            }
        }

        binding.btnFirst.setOnClickListener {
            val nextJunction = game.findPrevJunction()
            if (nextJunction!!.isFirstMove) {
                game.jump(nextJunction)
            } else {
                showJunctionInfoSnack(R.string.found_junction_snack_for_first)
                game.jump(nextJunction.nextMoveVariations[0])
            }
        }

        binding.btnFirst.setOnLongClickListener {
            game.jump(game.findFirstMove())
            true
        }

        binding.btnLast.setOnClickListener {
            val nextJunction = game.findNextJunction()
            if (nextJunction!!.hasNextMove()) {
                showJunctionInfoSnack(R.string.found_junction_snack_for_last)
                game.jump(nextJunction.nextMoveVariations[0])
            } else {
                game.jump(nextJunction)
            }
        }

        binding.btnLast.setOnLongClickListener {
            game.jump(game.findLastMove())
            true
        }
    }

    override fun onGoGameChanged(gameChangedEvent: GameChangedEvent?) {
        super.onGoGameChanged(gameChangedEvent)
        updateButtonStates()
    }

    private fun updateButtonStates() {
        setImageViewState(game.canUndo(), binding.btnFirst, binding.btnPrev)
        setImageViewState(game.canRedo(), binding.btnNext, binding.btnLast)
        bindButtonToMove(game.nextVariationWithOffset(-1), binding.btnPreviousVar)
        bindButtonToMove(game.nextVariationWithOffset(1), binding.btnNextVar)
    }

    private fun bindButtonToMove(move: GoMove?, button: ImageView) {
        setImageViewState(move != null, button)
        button.setOnClickListener { game.jump(move) }
    }

    private fun setImageViewState(state: Boolean, vararg views: ImageView) {
        views.forEach {
            it.isEnabled = state
            it.alpha = if (state) 1f else 0.4f
        }
    }

    private fun showJunctionInfoSnack(found_junction_snack_for_last: Int) {
        if (!GoPrefs.hasAcknowledgedJunctionInfo) {
            Snackbar.make(binding.btnLast, found_junction_snack_for_last, Snackbar.LENGTH_LONG).setAction(android.R.string.ok) { GoPrefs.hasAcknowledgedJunctionInfo = true }.show()
        }
    }

}
