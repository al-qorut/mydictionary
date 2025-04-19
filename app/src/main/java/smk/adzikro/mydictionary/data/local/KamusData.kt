package smk.adzikro.mydictionary.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import smk.adzikro.mydictionary.BuildConfig
import smk.adzikro.mydictionary.data.models.Kamus
import smk.adzikro.mydictionary.data.models.KamusDao
import smk.adzikro.mydictionary.utils.InitialData
import smk.adzikro.mydictionary.utils.config

@Database(entities = [Kamus::class],version = 1,exportSchema = true)
abstract class KamusData : RoomDatabase(){
    abstract fun kamusDao() : KamusDao


    companion object{
        private var INSTANCE:KamusData ?=null
        private const val DATABASE_NAME = "kamus.db"

        @JvmStatic
        fun getKamusData(context: Context):KamusData?{
            val passphrase: ByteArray = SQLiteDatabase.getBytes(context.config.passChiper.toCharArray())
            val factory = SupportFactory(passphrase)
            if(INSTANCE==null){
                synchronized(KamusData::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, KamusData::class.java,
                        DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .openHelperFactory(factory)
                       // .createFromAsset(DATABASE_NAME)
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                GlobalScope.launch(Dispatchers.IO) {
                                    INSTANCE.let {
                                        it?.kamusDao()?.addKamus(InitialData.getArAr(context))
                                        it?.kamusDao()?.addKamus(InitialData.getArIn(context))
                                        it?.kamusDao()?.addKamus(InitialData.getIndEng(context))
                                        it?.kamusDao()?.addKamus(InitialData.getEngInd(context))
                                        it?.kamusDao()?.addKamus(InitialData.getEngEng(context))
                                        it?.kamusDao()?.addKamus(InitialData.getIndInd(context))
                                        InitialData.LIST_KAMUS.forEachIndexed { index, i ->
                                            Log.e("Index", "Index giliran ${index}")
                                            it?.kamusDao()?.addKamus(InitialData.getKamusArab(i, context))
                                        }

                                    }
                                }
                            }

                        })

                        .build()
                }
            }
            return INSTANCE
        }
    }
}