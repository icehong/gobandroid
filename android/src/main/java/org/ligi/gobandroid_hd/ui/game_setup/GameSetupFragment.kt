package org.ligi.gobandroid_hd.ui.game_setup

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.chibatching.kotpref.bulk
import org.ligi.gobandroid_hd.InteractionScope
import org.ligi.gobandroid_hd.R
import org.ligi.gobandroid_hd.databinding.GameSetupInnerBinding
import org.ligi.gobandroid_hd.logic.GoGame
import org.ligi.gobandroid_hd.ui.GoActivity
import org.ligi.gobandroid_hd.ui.GoPrefs
import org.ligi.gobandroid_hd.ui.fragments.GobandroidFragment

class GameSetupFragment : GobandroidFragment(), OnSeekBarChangeListener {
    private var _binding: GameSetupInnerBinding? = null
    private val binding get() = _binding!!

    val size_offset = 2
    var act_size = GoPrefs.lastBoardSize
    var act_handicap = GoPrefs.lastHandicap
    var act_lineWidth = GoPrefs.boardLineWidth

    private var wanted_size = act_size

    private var uiHandler = Handler()

    private fun setSize(size: Int) {
        wanted_size = size

        uiHandler.post(object : Runnable {
            override fun run() {
                if (act_size != wanted_size) {
                    act_size += if (act_size > wanted_size) -1 else 1
                    uiHandler.postDelayed(this, 16)
                }

                if (!requireActivity().isFinishing) {
                    refresh_ui()
                }
            }
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = GameSetupInnerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {

        binding.sizeSeek.setOnSeekBarChangeListener(this)
        binding.handicapSeek.setOnSeekBarChangeListener(this)
        binding.lineWidthSeek.setOnSeekBarChangeListener(this)

        binding.sizeButton9x9.setOnClickListener { setSize(9) }
        binding.sizeButton13x13.setOnClickListener { setSize(13) }
        binding.sizeButton19x19.setOnClickListener { setSize(19) }

        refresh_ui()
        super.onStart()
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

        if (seekBar === binding.sizeSeek && act_size != (progress + size_offset)) {
            setSize(progress + size_offset)
        } else if (seekBar === binding.handicapSeek) {
            act_handicap = progress.toByte().toInt()
        } else if (seekBar === binding.lineWidthSeek) {
            act_lineWidth = progress.toByte().toInt()
        }

        refresh_ui()
    }

    private fun isAnimating() = act_size != wanted_size

    /**
     * refresh the ui elements with values from act_size / act_handicap
     */
    fun refresh_ui() {

        binding.gameSizeLabel.text = getString(R.string.size) + " " + act_size + "x" + act_size

        if (!isAnimating()) {
            // only enable handicap seeker when the size is 9x9 or 13x13 or 19x19
            binding.handicapSeek.isEnabled = act_size == 9 || act_size == 13 || act_size == 19

            binding.handicapLabel.text = if (binding.handicapSeek.isEnabled) {
                getString(R.string.handicap) + " " + act_handicap
            } else {
                getString(R.string.handicap_only_for)
            }
        }

        // the checks for change here are important - otherwise samsung moment
        // will die here with stack overflow
        if (act_size - size_offset != binding.sizeSeek.progress) binding.sizeSeek.progress = act_size - size_offset

        if (act_handicap != binding.handicapSeek.progress) binding.handicapSeek.progress = act_handicap

        if (act_lineWidth != binding.lineWidthSeek.progress) binding.lineWidthSeek.progress = act_lineWidth

        if (interactionScope.mode === InteractionScope.Mode.GNUGO)
            binding.sizeSeek.max = 19 - size_offset

        GoPrefs.bulk {
            lastBoardSize = act_size
            lastHandicap = act_handicap
            boardLineWidth = act_lineWidth
        }

        if (gameProvider.get().size != act_size || gameProvider.get().handicap != act_handicap) {
            gameProvider.set(GoGame(act_size, act_handicap))
        }

        if (activity is GoActivity) {
            val board = (activity as GoActivity).binding.goBoard

            if (board != null) {
                board.regenerateStoneImagesWithNewSize()
                board.invalidate()

                board.setLineSize(binding.lineWidthSeek.progress.toFloat())
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
    }

}
