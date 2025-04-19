package smk.adzikro.mydictionary.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import smk.adzikro.mydictionary.R
import smk.adzikro.mydictionary.databinding.ActivitySettingsBinding
import smk.adzikro.mydictionary.utils.LILAC_THEME
import smk.adzikro.mydictionary.utils.MINT_THEME
import smk.adzikro.mydictionary.utils.config
import java.util.Locale

class SettingActivity : BaseActivity() {
    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding ?: throw NullPointerException("Binding is not initialized")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        try {
            setContentView(binding.root)
        }catch (e : Exception){
            Log.e("SettingActivity", "Error setting content view", e)
        }
        enableEdgeToEdge()
        hideSystemUi()
        setView()
        setValue()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setView() {
        binding.apply {
            setSupportActionBar(settingsToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.title = getString(R.string.action_settings)
            tvTema.isChecked = config.thema == LILAC_THEME
            tvCari.isChecked = config.isArtinclude
            config.font_size.let {
                when (it) {
                    "size_mini" -> {
                        sizeFont.check(R.id.size_mini)
                    }

                    "size_medium" -> {
                        sizeFont.check(R.id.size_medium)
                    }

                    "size_large" -> {
                        sizeFont.check(R.id.size_large)
                    }

                    else -> {
                        sizeFont.check(R.id.size_medium)
                    }
                }
            }

            config.font.let {
                when (it) {
                    "default_font" -> {
                        viewFont.check(R.id.font_default)
                    }

                    "font_amiri" -> {
                        viewFont.check(R.id.font_amiri)
                    }

                    "font_qalam" -> {
                        viewFont.check(R.id.font_qalam)

                    }

                    else -> {
                        viewFont.check(R.id.font_default)
                    }
                }
            }

            when (Locale.getDefault().language) {
                "id" -> setImageEnd(R.drawable.ic_flag_id)
                "en" -> setImageEnd(R.drawable.ic_flag_us)
                "ar" -> setImageEnd(R.drawable.ic_flag_sa)
            }

        }
    }

    private fun setValue() {
        binding.apply {
            tvTema.setOnCheckedChangeListener { _, isChecked ->
                config.thema = if (isChecked) {
                    LILAC_THEME
                } else {
                    MINT_THEME
                }
                setAppTheme(config.thema)
                recreate()
            }
            tvCari.setOnCheckedChangeListener { _, isChecked ->
                config.isArtinclude = isChecked
            }
            tvLanguage.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            viewFont.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.font_default -> config.font = "default_font"
                    R.id.font_amiri -> config.font = "font_amiri"
                    R.id.font_qalam -> config.font = "font_qalam"
                }
            }

            sizeFont.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.size_mini -> config.font_size = "size_mini"
                    R.id.size_medium -> config.font_size = "size_medium"
                    R.id.size_large -> config.font_size = "size_large"
                }
            }

        }
    }

    private fun setImageEnd(drawable: Int) {
        binding.tvLanguage.setCompoundDrawablesWithIntrinsicBounds(
            null, null, ContextCompat.getDrawable(this, drawable), null
        )
    }

    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}