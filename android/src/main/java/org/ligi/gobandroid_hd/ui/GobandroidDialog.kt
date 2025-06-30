package org.ligi.gobandroid_hd.ui

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout.LayoutParams
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.lazy
import org.ligi.gobandroid_hd.App
import org.ligi.gobandroid_hd.InteractionScope
import org.ligi.gobandroid_hd.R
import org.ligi.gobandroid_hd.databinding.DialogGobandroidBinding
import org.ligi.gobandroid_hd.model.GameProvider
import org.ligi.gobandroid_hd.ui.application.GoAndroidEnvironment

/**
 * A styled Dialog fit in the gobandroid style
 */
open class GobandroidDialog(context: Context) : Dialog(context, R.style.dialog_theme) {
    lateinit var pbinding: DialogGobandroidBinding ;

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    val positive_btn: Button by lazy {
        createButton().apply {
            pbinding.buttonContainer.addView(this)
        }
    }

    val negative_btn: Button by lazy {
        createButton().apply {
            pbinding.buttonContainer.addView(this)
        }
    }

    val settings: GoAndroidEnvironment by App.kodein.lazy.instance()
    val gameProvider: GameProvider by App.kodein.lazy.instance()
    val interactionScope: InteractionScope by App.kodein.lazy.instance()

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        pbinding = DialogGobandroidBinding.inflate(inflater)
        super.setContentView(pbinding.root)

        // this sounds misleading but behaves right - we just do not want to  start with keyboard open
        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

    }

    fun setIconResource(@DrawableRes icon: Int) {
        pbinding.dialogTitle.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
    }

    override fun setContentView(@LayoutRes content: Int) {
        pbinding.dialogContent.addView(inflater.inflate(content, pbinding.dialogContent, false))
    }

    override fun setTitle(title: CharSequence?) {
        pbinding.dialogTitle.text = title
    }

    fun setPositiveButton(@StringRes text: Int, listener: (dialog: Dialog) -> Unit = { dismiss() }) {
        positive_btn.setText(text)
        positive_btn.setOnClickListener{ listener(this) }
    }


    fun setNegativeButton(@StringRes text: Int, listener: (dialog: Dialog) -> Unit = { dismiss() }) {
        negative_btn.setText(text)
        negative_btn.setOnClickListener{ listener(this) }
    }

    private fun createButton(): Button {
        val res = Button(context)
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f)
        res.layoutParams = lp
        return res
    }

    private inner class DialogDiscardingOnClickListener : DialogInterface.OnClickListener {
        override fun onClick(dialogInterface: DialogInterface, i: Int) {
            dialogInterface.dismiss()
        }
    }
}
