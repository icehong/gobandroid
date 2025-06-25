package org.ligi.gobandroid_hd.ui

import android.os.Bundle
import org.ligi.gobandroid_hd.R
import org.ligi.gobandroid_hd.databinding.ProfileBinding
import org.ligi.gobandroid_hd.ui.application.GobandroidFragmentActivity

class BaseProfileActivity : GobandroidFragmentActivity() {
    private lateinit var binding: ProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)
        binding = ProfileBinding.bind(pbinding.contentFrame.getChildAt(0))

        setTitle(R.string.profile)

        supportActionBar?.setDisplayShowTitleEnabled(true)

        binding.rankEdit.setText(GoPrefs.rank)
        binding.usernameEdit.setText(GoPrefs.username)
    }

    override fun onPause() {
        GoPrefs.rank = binding.rankEdit.text.toString()
        GoPrefs.username = binding.usernameEdit.text.toString()
        super.onPause()
    }

}
