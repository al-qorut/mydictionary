package smk.adzikro.mydictionary

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import smk.adzikro.mydictionary.data.local.KamusData
import smk.adzikro.mydictionary.utils.config

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.e("App", "onCreate")
        if (config.isFirst) {
            Log.e("App", "onCreate config.first")
            GlobalScope.launch(Dispatchers.IO){
                val db = KamusData.getKamusData(this@App)
                val x = db?.kamusDao()?.getAll()
                Log.e("App", "onCreate count db $x")
                config.isFirst = false
            }
        }
    }
}