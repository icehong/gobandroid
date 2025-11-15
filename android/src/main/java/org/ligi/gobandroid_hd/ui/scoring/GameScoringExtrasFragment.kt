package org.ligi.gobandroid_hd.ui.scoring

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.ligi.gobandroid_hd.R
import org.ligi.gobandroid_hd.databinding.GameResultBinding
import org.ligi.gobandroid_hd.events.GameChangedEvent
import org.ligi.gobandroid_hd.logic.GoGameScorer
import org.ligi.gobandroid_hd.ui.fragments.GobandroidGameAwareFragment

class GameScoringExtrasFragment : GobandroidGameAwareFragment() {
    private var _binding: GameResultBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = GameResultBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGoGameChanged(gameChangedEvent: GameChangedEvent?) {
        super.onGoGameChanged(gameChangedEvent)
        requireActivity().runOnUiThread { refresh() }
    }

    private fun getCapturesString(captures: Int, deadStones: Int): String {
        val result = Integer.toString(captures)

        if (deadStones > 0) {
            return result + " + " + deadStones
        }

        return result
    }

    fun refresh() {
        if (activity == null) {
            return
        }
        val game = gameProvider.get()
        val scorer = game.scorer ?: return

        binding.resultTxt.text = getFinTXT(scorer)

        binding.territoryBlack.text = String.format("%d", scorer.territory_black)
        binding.territoryWhite.text = String.format("%d", scorer.territory_white)

        binding.capturesBlack.text = getCapturesString(game.capturesBlack, scorer.dead_white)
        binding.capturesWhite.text = getCapturesString(game.capturesWhite, scorer.dead_black)

        binding.komi.text = String.format("%.1f", game.komi)

        binding.finalBlack.text = String.format("%.1f", scorer.pointsBlack)
        binding.finalWhite.text = String.format("%.1f", scorer.pointsWhite)
    }

    private fun getFinTXT(scorer: GoGameScorer): String {
        if (scorer.pointsBlack > scorer.pointsWhite) {
            val finalPoints = scorer.pointsBlack - scorer.pointsWhite
            return getString(R.string.black_won_with) + String.format("%.1f", finalPoints) + getString(R.string._points_)
        }

        if (scorer.pointsWhite > scorer.pointsBlack) {
            val finalPoints = scorer.pointsWhite - scorer.pointsBlack
            return getString(R.string.white_won_with_) + String.format("%.1f", finalPoints) + getString(R.string._points_)
        }
        return resources.getString(R.string.game_ended_in_draw)
    }
}
