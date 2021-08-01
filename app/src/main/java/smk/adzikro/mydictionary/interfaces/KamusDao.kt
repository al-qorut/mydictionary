package smk.adzikro.mydictionary.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import smk.adzikro.mydictionary.models.Kamus

@Dao
interface KamusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addKamus(kamus: MutableList<Kamus>)

    @Query("SELECT * From kamus Where kata like :kata")
    fun getKamus(kata:String):Flowable<MutableList<Kamus>>?


}