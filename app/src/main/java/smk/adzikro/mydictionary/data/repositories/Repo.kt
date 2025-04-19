package smk.adzikro.mydictionary.data.repositories

import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import smk.adzikro.mydictionary.data.models.Kamus

interface Repo {
    fun getKamus(kata: String): Flowable<MutableList<Kamus>>
    fun getKamusPaging(kata: String): Observable<PagingData<Kamus>>

    fun addKamus(kamus: MutableList<Kamus>) :Completable

    fun getListKata(): Flowable<List<String>>
    fun updateKamus(kamus: Kamus) : Completable
    fun getFavorite(): Flowable<MutableList<Kamus>>
}