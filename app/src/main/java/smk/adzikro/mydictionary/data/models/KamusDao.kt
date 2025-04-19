package smk.adzikro.mydictionary.data.models

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface KamusDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addKamus(kamus: MutableList<Kamus>)

    @Query("SELECT * From kamus Where kata like '%' || :kata || '%' or arti like '%' || :kata || '%' ")
    fun getKamus(kata:String): Flowable<MutableList<Kamus>>

    @Query("SELECT * From kamus Where kata like '%' || :kata || '%' ")
    fun getKamusCari(kata:String): Flowable<MutableList<Kamus>>

    @Query("SELECT * From kamus Where kata like '%' || :kata || '%' LIMIT :pageSize OFFSET :offset")
    fun getKamusPaging(kata: String, pageSize: Int, offset: Int): Single<List<Kamus>>



    @Query("SELECT * From kamus Where kata like '%' || :kata || '%'")
    fun getKamusKata(kata:String): PagingSource<Int, Kamus>

    @Query("SELECT count() From kamus")
    fun getAll(): Long

    @Query("SELECT kata FROM kamus group by kata")
    fun getListKata() : Flowable<List<String>>

    @Update
    fun updateKamus(kamus: Kamus)

    @Query("SELECT * FROM kamus where favorite = 1")
    fun getFavorite(): Flowable<MutableList<Kamus>>

}