package smk.adzikro.mydictionary.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import smk.adzikro.mydictionary.R
import smk.adzikro.mydictionary.utils.MINT_THEME
import smk.adzikro.mydictionary.utils.config
import smk.adzikro.mydictionary.viewmodels.MainViewModel


@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()
    protected lateinit var currentTheme: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentTheme = config.thema
        setAppTheme(currentTheme)
        enableEdgeToEdge()
    }

    override fun onResume() {
        super.onResume()
        val selectedTheme = config.thema
        if(selectedTheme != currentTheme)
            recreate()
    }

    fun setAppTheme(currentTheme: String) {
        when (currentTheme) {
            MINT_THEME -> setTheme(R.style.Theme_App_Mint)
            else -> setTheme(R.style.Theme_App_Lilac)
        }
    }
}