package smk.adzikro.mydictionary.repositories

import android.content.Context
import io.reactivex.Completable
import io.reactivex.Flowable
import smk.adzikro.mydictionary.models.Kamus

class DataSource(context: Context){
    private var kamus  = KamusData.getKamusData(context)

    fun addKamus(data:MutableList<Kamus>) = Completable.fromAction {
        kamus?.kamusDao()?.addKamus(data)
    }

    fun getKamus(kata:String): Flowable<MutableList<Kamus>>? {
        return kamus?.kamusDao()?.getKamus(kata)
    }

}