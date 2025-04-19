package smk.adzikro.mydictionary.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import smk.adzikro.mydictionary.R
import smk.adzikro.mydictionary.databinding.ActivityMainBinding
import smk.adzikro.mydictionary.ui.fragments.ResultFragment.Companion.KATA_CARI
import smk.adzikro.mydictionary.utils.AppUpdateManagerUtil
import smk.adzikro.mydictionary.utils.share

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!
    var kata = ""
    private lateinit var appUpdateManagerUtil : AppUpdateManagerUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
        val nav = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(nav)
        hideSystemUi()
        setupView()
        appUpdateManagerUtil = AppUpdateManagerUtil(this)
        appUpdateManagerUtil.cekUpdate()
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

    private fun setupView() {
        binding.apply {
            menuMain.getToolbar().inflateMenu(R.menu.menu_main)
            menuMain.toggleHideOnScroll(false)
            menuMain.setupMenu()
            menuMain.getToolbar().menu.apply {
                findItem(R.id.action_search).isVisible = false
                findItem(R.id.action_share).isVisible = true
                findItem(R.id.action_settings).isVisible = true
            }
            menuMain.onSearchClosedListener = {
                //getAllFragments().forEach {
                //    it?.searchQueryChanged("")
                // }
            }
            menuMain.onSearchTextChangedListener = { text ->
                if (!text.isEmpty()) {
                    // cariKata(text)
                    kata = text
                    menuMain.getToolbar().menu.findItem(R.id.action_search).isVisible = true
                } else {
                    kata = ""
                    menuMain.getToolbar().menu.findItem(R.id.action_search).isVisible = false
                }
            }
            menuMain.onRunSearch = { text ->
                text.let {
                    kata = it
                    cariKata(it)
                    menuMain.closeSearch()
                }
            }
            menuMain.getToolbar().setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_settings -> showSetting()
                    R.id.action_share -> shareApp()
                    R.id.action_search -> {
                        cariKata(kata)
                        menuMain.closeSearch()
                    }

                    else -> return@setOnMenuItemClickListener false
                }
                return@setOnMenuItemClickListener true
            }
        }
    }

    private fun searchKata(kata: String) {
        viewModel.getData(kata)
    }

    fun getFavorite() {
        viewModel.getFavorite()
    }

    private fun showSetting() {
        startActivity(Intent(this, SettingActivity::class.java))
    }

    private fun shareApp() {
        // toast(this, "under cunstruction")
        share(this)
    }

    private fun cariKata(cari: String) {
        val navController = findNavController(R.id.main_nav_host)
        val bundle = Bundle().apply {
            putString(KATA_CARI, cari)
        }
        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)  // Jika fragment sudah ada di back stack, jangan buat fragment baru
            .build()
        navController.navigate(R.id.resultFragment, bundle, navOptions)
    }

    fun isLoading(isLoading: Boolean) {
        isLoading.let {
            binding.progressBarResult.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onBackPressed() {
        if (binding.menuMain.isSearchOpen) {
            binding.menuMain.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

}