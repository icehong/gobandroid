/**

 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation;

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http:></http:>//www.gnu.org/licenses/>.

 */

package org.ligi.gobandroid_hd.ui.review


import org.ligi.gobandroid_hd.R
import org.ligi.gobandroid_hd.databinding.EndReviewDialogBinding
import org.ligi.gobandroid_hd.ui.GobandroidDialog
import org.ligi.gobandroid_hd.ui.GobandroidNotifications
import org.ligi.gobandroid_hd.ui.application.GobandroidFragmentActivity
import org.ligi.gobandroid_hd.ui.sgf_listing.GoLink

/**
 * Dialog to show when user wants to set a BookMark

 * @author [Marcus -Ligi- Bueschleb](http://ligi.de)
 * *
 *
 *
 * *         License: This software is licensed with GPLv3
 */
class EndReviewDialog(context: GobandroidFragmentActivity) : GobandroidDialog(context) {
    private val binding: EndReviewDialogBinding

    private val meta by lazy { SGFMetaData(gameProvider.get()) }

    init {
        setContentView(R.layout.end_review_dialog)
        binding = EndReviewDialogBinding.bind(pbinding.dialogContent.getChildAt(0))

        setTitle(R.string.end_review)
        setIconResource(R.drawable.ic_action_help_outline)

        binding.saveBookmarkCp.isChecked = true

        binding.bookmarkNameEt.setText(BookmarkDialog.getCleanEnsuredFilename(settings, gameProvider.get()))

        if (meta.rating != null) {
            binding.gameRating.rating = .5f * meta.rating!!
        }

        binding.saveBookmarkCp.setOnCheckedChangeListener { _, isChecked ->
            binding.bookmarkNotificationCb.isEnabled = isChecked
            binding.bookmarkNameEt.isEnabled = isChecked
        }

        setPositiveButton(R.string.end_review_ok_button, { dialog ->

            if (binding.saveBookmarkCp.isChecked) {
                GoLink.saveGameToGoLink(gameProvider.get(), settings.bookmarkPath, binding.bookmarkNameEt.text.toString() + ".golink")
            }

            if (binding.bookmarkNotificationCb.isChecked) {
                GobandroidNotifications(context).addGoLinkNotification(settings.bookmarkPath.toString() + "/" + binding.bookmarkNameEt.text.toString() + ".golink")
            }

            saveSGFMeta()
            dialog.dismiss()
            context.finish()
        })

        setNegativeButton(R.string.end_review_stay_button, { dialog ->
            dialog.dismiss()
            saveSGFMeta()
        })
    }

    fun saveSGFMeta() {
        meta.rating = (binding.gameRating.rating * 2).toInt()
        meta.persist()
    }
}
