package org.ligi.gobandroid_hd.ui.go_terminology

import android.app.Activity
import android.text.util.Linkify
import android.view.LayoutInflater
import android.widget.TextView

import org.ligi.gobandroid_hd.R
import org.ligi.gobandroid_hd.databinding.GoTermsViewBinding
import org.ligi.gobandroid_hd.ui.GobandroidDialog
import timber.log.Timber

class GoTerminologyDialog(context: Activity, term: String) : GobandroidDialog(context) {
    private val binding: GoTermsViewBinding

    init {
        setTitle(term)
        setIconResource(R.drawable.ic_action_info_outline_wrapped)
        setContentView(R.layout.go_terms_view)
        binding = GoTermsViewBinding.bind(pbinding.dialogContent.getChildAt(0))

        val termMap = GoTerminologyViewActivity.Term2resMap
        if (termMap.containsKey(term)) {
            binding.goTermsText.setText(termMap[term]!!)
        } else {
            binding.goTermsText.setText(R.string.no_definition_found)
            Timber.w("no definition found for " + term)
        }

        Linkify.addLinks(binding.goTermsText, Linkify.ALL)

    }

}
