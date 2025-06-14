package org.ligi.gobandroid_hd.ui.editing

import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.BaseAdapter
import org.ligi.gobandroid_hd.R
import org.ligi.gobandroid_hd.databinding.EditExtrasBinding
import org.ligi.gobandroid_hd.events.GameChangedEvent
import org.ligi.gobandroid_hd.ui.editing.model.EditGameMode
import org.ligi.gobandroid_hd.ui.fragments.GobandroidGameAwareFragment
import org.ligi.kaxt.doAfterEdit

class EditGameExtrasFragment : GobandroidGameAwareFragment() {
    private var _binding: EditExtrasBinding? = null
    private val binding get() = _binding!!

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val editModePool = (activity as EditGameActivity).statefulEditModeItems
        _binding = EditExtrasBinding.inflate(inflater, container, false)

        val editModeAdapter = EditModeButtonsAdapter(editModePool)
        binding.gridView.adapter = editModeAdapter

        binding.gridView.onItemClickListener = OnItemClickListener { adapter, _, position, _ ->
            editModePool.setModeByPosition(position)
            (adapter.adapter as BaseAdapter).notifyDataSetChanged()
        }

        binding.editSwitch.isChecked = true

        binding.editSwitch.setOnCheckedChangeListener { _, isChecked ->
            binding.gridView.visibility = if (isChecked) View.VISIBLE else View.GONE
            editModePool.mode = if (isChecked) EditGameMode.BLACK else EditGameMode.PLAY
            editModeAdapter.notifyDataSetChanged()
        }
        binding.commentEt.setText(gameProvider.get().actMove.comment)
        binding.commentEt.setHint(R.string.enter_your_comments_here)
        binding.commentEt.gravity = Gravity.TOP
        binding.commentEt.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color_on_board_bg))

        binding.commentEt.doAfterEdit {
            gameProvider.get().actMove.comment = it.toString()
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGoGameChanged(gameChangedEvent: GameChangedEvent?) {
        super.onGoGameChanged(gameChangedEvent)
        activity?.runOnUiThread { binding.commentEt.setText(gameProvider.get().actMove.comment) }
    }

}
