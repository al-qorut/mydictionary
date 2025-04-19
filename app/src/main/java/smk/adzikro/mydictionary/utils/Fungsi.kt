package smk.adzikro.mydictionary.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import smk.adzikro.mydictionary.R
import java.io.File
import java.io.FileOutputStream

fun isArabic(input: String): Boolean {
    val arabRegex = "[\u0600-\u06FF]".toRegex()
    return arabRegex.containsMatchIn(input)
}

val Context.config: Config get() = Config.newInstance(applicationContext)

fun setTextKata(cari : String, kata :String) : SpannableString {
    val spannableString = SpannableString(kata)
    val start = kata.indexOf(cari)
    if (start != -1) {
        val end = start + cari.length
        spannableString.setSpan(ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return spannableString
}
fun highlightHtmlText(htmlText: String, cari: String): SpannableString {
    val spanned = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    val spannable = SpannableString(spanned)
    val plainText = spanned.toString()

    var start = plainText.indexOf(cari, ignoreCase = true)
    while (start != -1) {
        val end = start + cari.length
        spannable.setSpan(
            ForegroundColorSpan(Color.RED),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        start = plainText.indexOf(cari, end, ignoreCase = true)
    }

    return spannable
}

fun captureViewAsBitmap(view: View): Bitmap {
    val width = View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
    val height = View.MeasureSpec.makeMeasureSpec(view.height, View.MeasureSpec.EXACTLY)
    view.measure(width, height)
    view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}
fun saveBitmapToCache(bitmap: Bitmap, context: Context): Uri {
    val cachePath = File(context.cacheDir, "images")
    if (!cachePath.exists()) {
        cachePath.mkdirs()  // Membuat direktori jika belum ada
    }
    val file = File(cachePath, "shared_image.png")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.close()
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}

fun shareImage(context: Context, bitmap: Bitmap) {
    val uri = saveBitmapToCache(bitmap, context)
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "image/png"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share image"))
}


fun toast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun getFont(context: Context) : Typeface? {
    context.config.font.let {
        when (it) {
            "default_font" -> return Typeface.DEFAULT
            "font_amiri" -> return ResourcesCompat.getFont(context, R.font.amiri)
            "font_qalam" -> return ResourcesCompat.getFont(context, R.font.qalam)
            else -> return Typeface.DEFAULT
        }
    }
}

fun getFontSize(context: Context) : Float{
    context.config.font_size.let {
        when (it) {
            "default_size" -> return 14f
            "size_mini" -> return 12f
            "size_medium" -> return 16f
            "size_large" -> return 18f
            else -> return 14f
        }
    }
}

fun getFontSizeAr(context: Context) : Float{
    context.config.font_size.let {
        when (it) {
            "default_size" -> return 16f
            "size_mini" -> return 16f
            "size_medium" -> return 24f
            "size_large" -> return 28f
            else -> return 16f
        }
    }
}
fun share(context: Context) {
    try {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_SUBJECT, "Aplikasi Kamus")
        val sAux ="Aplikasi Kamus\nhttps://play.google.com/store/apps/details?id=smk.adzikro.mydictionary"
        i.putExtra(Intent.EXTRA_TEXT, sAux)
        context.startActivity(Intent.createChooser(i, "Pilih.."))
    } catch (e: Exception) { //e.toString();
    }
}

fun setClipboard(context: Context, text: String) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.text = text
    } else {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
    }
    toast(context, text+"\n Copied")
}