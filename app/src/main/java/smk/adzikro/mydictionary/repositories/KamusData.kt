package smk.adzikro.mydictionary.repositories

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import smk.adzikro.mydictionary.interfaces.KamusDao
import smk.adzikro.mydictionary.models.Kamus

@Database(entities = [Kamus::class],version = 1,exportSchema = false)
abstract class KamusData :RoomDatabase(){
    abstract fun kamusDao() : KamusDao
    companion object{
        var INSTANCE:KamusData ?=null

        fun getKamusData(context: Context):KamusData?{
            if(INSTANCE==null){
                synchronized(KamusData::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, KamusData::class.java,"kamus.db").build()
                }
            }
            return INSTANCE
        }
    }
}