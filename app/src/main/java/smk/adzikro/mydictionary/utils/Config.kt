package smk.adzikro.mydictionary.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import javax.inject.Inject

open class Config @Inject constructor(val context: Context) {
   // protected val prefs = context.getSharedPreferences("config", Context.MODE_PRIVATE)
    protected val prefs = EncryptedSharedPreferences.create(
        "config",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        fun newInstance(context: Context) = Config(context)
    }
    var thema: String
        get() = prefs.getString(KEY_CURRENT_THEME, MINT_THEME).toString()
        set(value) = prefs.edit().putString(KEY_CURRENT_THEME, value).apply()

    var isArtinclude : Boolean
        get() = prefs.getBoolean(KEY_IS_ARTI_VISIBLE, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_ARTI_VISIBLE, value).apply()

    var isFirst : Boolean
        get() = prefs.getBoolean(KEY_IS_RUN_FIRST, true)
        set(value) = prefs.edit().putBoolean(KEY_IS_RUN_FIRST, value).apply()

    var font : String
        get() = prefs.getString(KEY_FONT, DEFAULT_FONT).toString()
        set(value) = prefs.edit().putString(KEY_FONT, value).apply()

    var font_size : String
        get() = prefs.getString(KEY_FONT_SIZE, DEFAULT_FONT_SIZE).toString()
        set(value) = prefs.edit().putString(KEY_FONT_SIZE, value).apply()

    var pageSize : Int
        get() = prefs.getInt(KEY_PAGE_SIZE, DEFAULT_PAGE_SIZE)
        set(value) = prefs.edit().putInt(KEY_PAGE_SIZE, value).apply()

    var passChiper : String
        get() = prefs.getString(KEY_PASS_CHIPER, "!Al78Qorut").toString()
        set(value) = prefs.edit().putString(KEY_PASS_CHIPER, value).apply()

}