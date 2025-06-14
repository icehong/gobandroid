package org.ligi.gobandroid_hd.ui.tsumego

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.lazy
import org.ligi.compat.HtmlCompat
import org.ligi.gobandroid_hd.App
import org.ligi.gobandroid_hd.R
import org.ligi.gobandroid_hd.databinding.GameExtraTsumegoBinding
import org.ligi.gobandroid_hd.model.GameProvider
import org.ligi.gobandroid_hd.ui.go_terminology.GoTerminologyViewActivity

class TsumegoGameExtrasFragment : Fragment() {
    private var _binding: GameExtraTsumegoBinding? = null
    private val binding get() = _binding!!

    internal val gameProvider: GameProvider  by App.kodein.lazy.instance()


    private var off_path_visible = false
    private var correct_visible = false

    private fun updateUI() {
        if (activity == null) { // views not yet created
            return  // will come back later
        }

        val game = gameProvider.get()

        requireActivity().runOnUiThread {
            binding.tsumegoOffPathView.visibility = if (off_path_visible) TextView.VISIBLE else TextView.GONE

            if (correct_visible) {
                binding.tsumegoCorrectView.visibility = View.VISIBLE
                val optionalNextTsumegoURLString = NextTsumegoFileFinder.calcNextTsumego(game.metaData
                        .fileName
                        .replaceFirst("file://".toRegex(), ""))

                if (optionalNextTsumegoURLString != null) {

                    binding.tsumegoCorrectView.movementMethod = LinkMovementMethod.getInstance()

                    val text = getString(R.string.tsumego_correct) +
                            " <a href='tsumego://" +
                            optionalNextTsumegoURLString +
                            "'>" +
                            getString(R.string.next_tsumego) +
                            "</a>"
                    binding.tsumegoCorrectView.text = HtmlCompat.fromHtml(text)
                } else {
                    binding.tsumegoCorrectView.text = getString(R.string.correct_but_no_more_tsumegos)
                }
            } else {
                binding.tsumegoCorrectView.visibility = View.GONE
            }

            // the 10 is a bit of a magic number - just want to show comments that
            // have extras here to prevent double commentView written - but sometimes
            // there is more info in the commentView
            if (!correct_visible && game.actMove.comment.length > 10) {
                binding.gameComment.visibility = View.VISIBLE
                binding.gameComment.text = game.actMove.comment
                if (!TextUtils.isEmpty(game.actMove.comment)) {
                    GoTerminologyViewActivity.linkifyTextView(binding.gameComment)
                }
            } else {
                binding.gameComment.visibility = View.GONE
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = GameExtraTsumegoBinding.inflate(inflater, container, false)
        updateUI()
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setOffPathVisibility(visible: Boolean) {
        off_path_visible = visible
        updateUI()
    }

    fun setCorrectVisibility(visible: Boolean) {
        correct_visible = visible
        updateUI()
    }

}
