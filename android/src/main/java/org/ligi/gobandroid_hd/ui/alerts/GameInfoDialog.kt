/**
 * gobandroid
 * by Marcus -Ligi- Bueschleb
 * http://ligi.de
 *
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation;
 *
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http:></http:>//www.gnu.org/licenses/>.
 */

package org.ligi.gobandroid_hd.ui.alerts

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import android.view.View
import org.ligi.gobandroid_hd.R
import org.ligi.gobandroid_hd.databinding.DialogGameForwardBinding
import org.ligi.gobandroid_hd.databinding.GameInfoBinding
import org.ligi.gobandroid_hd.logic.GoGame
import org.ligi.gobandroid_hd.ui.BaseProfileActivity
import org.ligi.gobandroid_hd.ui.GoPrefs
import org.ligi.gobandroid_hd.ui.GobandroidDialog
import org.ligi.kaxt.doAfterEdit
import org.ligi.kaxt.setVisibility
import org.ligi.kaxt.startActivityFromClass

/**
 * Class to show an Alert with the Game Info ( who plays / rank / game name .. )
 */
class GameInfoDialog(context: Context, game: GoGame) : GobandroidDialog(context) {
    private var binding: GameInfoBinding = GameInfoBinding.inflate(LayoutInflater.from(context))


    private fun checkUserNamePresent(): Boolean {
        if (GoPrefs.username.isEmpty()) {
            context.startActivityFromClass(BaseProfileActivity::class.java)
            return false
        }
        return true
    }

    init {
        setTitle(R.string.game_info)
        setIconResource(R.drawable.ic_action_info_outline)
        setContentView(binding.root)

        binding.blackNameEt.doAfterEdit {
            updateItsMeButtonVisibility()
        }

        binding.whiteNameEt.doAfterEdit {
            updateItsMeButtonVisibility()
        }

        binding.userIsWhiteBtn.setOnClickListener {
            if (checkUserNamePresent()) {
                binding.whiteNameEt.setText(GoPrefs.username)
                binding.whiteRankEt.setText(GoPrefs.rank)
            }
        }

        binding.userIsBlackBtn.setOnClickListener {
            if (checkUserNamePresent()) {
                binding.blackNameEt.setText(GoPrefs.username)
                binding.blackRankEt.setText(GoPrefs.rank)
            }
        }

        binding.buttonKomiSeven.setOnClickListener {
            binding.komiEt.setText("7.5")
        }

        binding.buttonKomiSix.setOnClickListener {
            binding.komiEt.setText("6.5")
        }


        binding.buttonKomiFive.setOnClickListener {
            binding.komiEt.setText("5.5")
        }
        binding.gameNameEt.setText(game.metaData.name)
        binding.blackNameEt.setText(game.metaData.blackName)
        binding.blackRankEt.setText(game.metaData.blackRank)
        binding.whiteNameEt.setText(game.metaData.whiteName)
        binding.whiteRankEt.setText(game.metaData.whiteRank)
        binding.komiEt.setText(game.komi.toString())
        binding.gameResultEt.setText(game.metaData.result)
        binding.gameDifficultyEt.setText(game.metaData.difficulty)
        binding.gameDateEt.setText(game.metaData.date)

        updateItsMeButtonVisibility()

        setPositiveButton(android.R.string.ok, { dialog ->
            game.metaData.name = binding.gameNameEt.text.toString()
            game.metaData.blackName = binding.blackNameEt.text.toString()
            game.metaData.blackRank = binding.blackRankEt.text.toString()
            game.metaData.whiteName = binding.whiteNameEt.text.toString()
            game.metaData.whiteRank = binding.whiteRankEt.text.toString()
            game.metaData.date = binding.gameDateEt.text.toString()

            try {
                game.komi = java.lang.Float.valueOf(binding.komiEt.text.toString())
            } catch (ne: NumberFormatException) {
                AlertDialog.Builder(context).setMessage(R.string.komi_must_be_a_number)
                        .setPositiveButton(android.R.string.ok, null)
                        .setTitle(R.string.problem)
                        .show()
            }

            game.metaData.result = binding.gameResultEt.text.toString()
            dialog.dismiss()
        })
    }

    private fun updateItsMeButtonVisibility() {
        binding.userIsWhiteBtn.setVisibility(binding.whiteNameEt.text.toString().isEmpty())
        binding.userIsBlackBtn.setVisibility(binding.blackNameEt.text.toString().isEmpty())
    }
}