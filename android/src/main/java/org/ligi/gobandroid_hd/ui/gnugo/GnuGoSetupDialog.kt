package org.ligi.gobandroid_hd.ui.gnugo

import android.content.Context
import android.preference.PreferenceManager
import org.ligi.gobandroid_hd.R
import org.ligi.gobandroid_hd.databinding.SaveBookmarkBinding
import org.ligi.gobandroid_hd.databinding.SetupGnugoBinding
import org.ligi.gobandroid_hd.ui.GobandroidDialog
import org.ligi.kaxt.doOnProgressChanged

class GnuGoSetupDialog(context: Context) : GobandroidDialog(context) {
    private lateinit var binding: SetupGnugoBinding

    private val shared_prefs by lazy { PreferenceManager.getDefaultSharedPreferences(context) }

    init {
        binding = SetupGnugoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setTitle(R.string.gnugo)
        setIconResource(R.drawable.ic_action_settings)

        if (shared_prefs.getBoolean(SP_KEY_PLAYS_BOTH, false)) {
            binding.gnugoPlaysBothRadio.isChecked = true
        } else if (shared_prefs.getBoolean(SP_KEY_PLAYS_WHITE, false)) {
            binding.gnugoPlaysWhiteRadio.isChecked = true
        } else if (shared_prefs.getBoolean(SP_KEY_PLAYS_BLACK, false)) {
            binding.gnugoPlaysBlackRadio.isChecked = true
        } else {
            // no former selection - default to black
            binding.gnugoPlaysBlackRadio.isChecked = true
        }

        var level = shared_prefs.getInt(SP_KEY_STRENGTH, 0)

        if (level > binding.gnugoStrengthSeek.max) {
            level = binding.gnugoStrengthSeek.max
        }

        binding.gnugoStrengthSeek.progress = level

        binding.gnugoStrength.text = getContext().getString(R.string.gnugo_strength) + " " + level.toString()

        binding.gnugoStrengthSeek.doOnProgressChanged { progress, fromUser ->
            if (fromUser) {
                binding.gnugoStrength.text = getContext().getString(R.string.gnugo_strength) + progress.toString()
            }
        }
    }

    fun isWhiteActive() = binding.gnugoPlaysWhiteRadio.isChecked
    fun isBlackActive() = binding.gnugoPlaysBlackRadio.isChecked
    fun isBothActive() = binding.gnugoPlaysBothRadio.isChecked
    fun strength() = binding.gnugoStrengthSeek.progress

    fun saveRecentAsDefault() {
        val edit = shared_prefs.edit()
        edit.putInt(SP_KEY_STRENGTH, strength())

        edit.putBoolean(SP_KEY_PLAYS_WHITE, isWhiteActive())
        edit.putBoolean(SP_KEY_PLAYS_BLACK, isBlackActive())
        edit.putBoolean(SP_KEY_PLAYS_BOTH, isBothActive())

        edit.apply()
    }

    companion object {

        private val SP_KEY_PLAYS_BLACK = "plays_black"
        private val SP_KEY_PLAYS_WHITE = "plays_white"
        private val SP_KEY_PLAYS_BOTH = "plays_both"

        private val SP_KEY_STRENGTH = "strength"
    }

}
