package smk.adzikro.mydictionary.tools

import android.app.Activity
import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import smk.adzikro.mydictionary.R
import smk.adzikro.mydictionary.databinding.LayoutDialogBinding
import androidx.appcompat.app.AppCompatActivity
import smk.adzikro.mydictionary.tools.AlqDialog.Companion.binding

//import kotlinx.android.synthetic.main.layout_dialog.*

class AlqDialog {

    /***
     * Positions For Alert Dialog
     * */
    enum class POSITIONS {
        CENTER, BOTTOM
    }

    companion object {

        private lateinit var layoutInflater: LayoutInflater
        lateinit var binding: LayoutDialogBinding
        /***
         * core method For Alert Dialog
         * */
        fun build(
            context: Activity
        ): AlertDialog {
            layoutInflater = LayoutInflater.from(context)
            binding = LayoutDialogBinding.inflate(layoutInflater)
            val alertDialog =
                AlertDialog.Builder(
                    context, R.style.full_screen_dialog
                )
                    .setView(binding.root)//R.layout.layout_dialog)
            val alert: AlertDialog = alertDialog.create()
            // Let's start with animation work. We just need to create a style and use it here as follows.
            //Pop In and Pop Out Animation yet pending
            //alert.window?.attributes?.windowAnimations = R.style.SlidingDialogAnimation
            alert.show()
            return alert
        }
    }
}

/***
 * Title Properties For Alert Dialog
 * */
fun AlertDialog.title(
    title: String,
    fontStyle: Typeface? = null,
    titleColor: Int = 0
): AlertDialog {
    binding.title.text = title.trim()
    if (fontStyle != null) {
        binding.title.typeface = fontStyle
    }
    if (titleColor != 0) {
        binding.title.setTextColor(titleColor)
    }
    binding.title.show()
    return this
}

/***
 * Dialog Background properties For Alert Dialog
 * */
fun AlertDialog.background(
    dialogBackgroundColor: Int? = null
): AlertDialog {
    if (dialogBackgroundColor != null) {
        binding.mainLayout.setBackgroundResource(dialogBackgroundColor)
    }
    return this
}

/***
 * Positions of Alert Dialog
 * */
fun AlertDialog.position(
    position: AlqDialog.POSITIONS = AlqDialog.POSITIONS.BOTTOM
): AlertDialog {
    val layoutParams = binding.mainLayout.layoutParams as RelativeLayout.LayoutParams
    if (position == AlqDialog.POSITIONS.CENTER) {
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
    } else if (position == AlqDialog.POSITIONS.BOTTOM) {
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
    }
    binding.mainLayout!!.layoutParams = layoutParams
    return this
}

/***
 * Sub Title or Body of Alert Dialog
 * */
fun AlertDialog.body(
    body: String,
    fontStyle: Typeface? = null,
    color: Int = 0
): AlertDialog {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        binding.subHeading.text= Html.fromHtml(body, 0)
    } else {
        binding.subHeading.text= Html.fromHtml(body)
    }
    //this.subHeading.text = getHtml(body.trim())
    binding.subHeading.show()
    if (fontStyle != null) {
        binding.subHeading.typeface = fontStyle
    }
    if (color != 0) {
        binding.subHeading.setTextColor(color)
    }
    return this
}

/***
 * Icon of  Alert Dialog
 * */
fun AlertDialog.icon(
    icon: Int,
    animateIcon: Boolean = false
): AlertDialog {
    binding.image.setImageResource(icon)
    binding.image.show()
    // Pulse Animation for Icon
    if (animateIcon) {
        val pulseAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse)
        binding.image.startAnimation(pulseAnimation)
    }
    return this
}

/***
 * onPositive Button Properties For Alert Dialog
 * */
fun AlertDialog.onPositive(
    text: String,
    buttonBackgroundColor: Int? = null,
    textColor: Int? = null,
    action: (() -> Unit)? = null
): AlertDialog {
    binding.yesButton.show()
    if (buttonBackgroundColor != null) {
        binding.yesButton.setBackgroundResource(buttonBackgroundColor)
    }
    if (textColor != null) {
        binding.yesButton.setTextColor(textColor)
    }
    binding.yesButton.text = text.trim()
    binding.yesButton.setOnClickListener {
        action?.invoke()
        dismiss()
    }
    return this
}

/***
 * onNegative Button Properties For Alert Dialog
 * */
fun AlertDialog.onNegative(
    text: String,
    buttonBackgroundColor: Int? = null,
    textColor: Int? = null,
    action: (() -> Unit)? = null
): AlertDialog {
    binding.noButton.show()
    binding.noButton.text = text.trim()
    if (textColor != null) {
        binding.noButton.setTextColor(textColor)
    }
    if (buttonBackgroundColor != null) {
        binding.noButton.setBackgroundResource(buttonBackgroundColor)
    }
    binding.noButton.setOnClickListener {
        action?.invoke()
        dismiss()
    }
    return this
}

private fun View.show() {
    this.visibility = View.VISIBLE
}
