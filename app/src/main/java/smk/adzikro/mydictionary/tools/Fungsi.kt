package smk.adzikro.mydictionary.tools

import android.annotation.SuppressLint
import android.content.*
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import smk.adzikro.mydictionary.R


val huruf_ar: Array<String> =
    arrayOf<String>(
        "ا", "ب", "ج", "د", "ه", "و", "ز",
        "ح", "ط", "ي", "ك", "ل", "م",
        "ن", "س", "ع", "ف", "ص", "ق",
        "ر", "ش", "ت", "ث", "خ", "ذ", "ض", "ظ", "غ"
    )
fun isArabic(a: String): Boolean {
    var f = false
    if (a.length > 1) {
        f = true
    } else {
        for (i in 0 until huruf_ar.size) {
            if (huruf_ar[i].equals(a)) {
                f = true
                break
            }
        }
    }
    return f
}

fun getHtml(htmlBody: String): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(htmlBody, 0).toString()
    else
        Html.fromHtml(htmlBody).toString()
}

fun isPertama(context: Context):Boolean {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    return sharedPreferences.getBoolean("pertama", true)
}
fun setPertama(x:Boolean, context: Context){
    PreferenceManager.getDefaultSharedPreferences(context).edit {
        putBoolean("pertama",x)
        commit()
        apply()
    }
}

fun getVersi(context: Context): String {
    var versi = ""
    val manager = context.packageManager
    try {
        val info = manager.getPackageInfo(context.packageName, 0)
        versi = info.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return versi
}

@SuppressLint("ServiceCast")
fun setClipboard(context: Context, text: String) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.text = text
    } else {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
    }
    Toast.makeText(context, String.format(context.getString(R.string.copytoclip), text),Toast.LENGTH_SHORT).show()
}
fun applicationName(con:Context): String{
        val pm =con.packageManager
        val ai: ApplicationInfo
        var appName: String
        try {
            ai = pm.getApplicationInfo("smk.adzikro.mydictionary", 0)
            appName = pm.getApplicationLabel(ai) as String
        } catch (e: PackageManager.NameNotFoundException) {
            appName = "(unknown)"
        }
        return appName
    }
fun showRate(context: Context) {
    try {
        startActivity(context,Intent(Intent.ACTION_VIEW, Uri
                    .parse("market://details?id="
                                + "smk.adzikro.mydictionary")),null)

    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context, context.getString(R.string.no_play_store),
            Toast.LENGTH_SHORT
        ).show()
    }
}
fun Rate(context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(context.getString(R.string.rate))
    builder.setMessage(String.format(context.getString(R.string.title_rate), applicationName(context)))
    builder.setPositiveButton(context.getString(R.string.done)) { dialog, which -> showRate(context) }
    builder.setNegativeButton(context.getString(R.string.no)) { dialog, which -> }.show()
}
fun Share(context: Context) {
    try {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(
            Intent.EXTRA_SUBJECT,
            context.applicationInfo.loadLabel(context.packageManager).toString()
        )
        val sAux = applicationName(context)
        i.putExtra(Intent.EXTRA_TEXT, sAux)
        context.startActivity(Intent.createChooser(i, context.getText(R.string.pilih)))
    } catch (e: Exception) { //e.toString();
    }
}