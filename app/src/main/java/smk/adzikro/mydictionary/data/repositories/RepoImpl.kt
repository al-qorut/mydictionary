package smk.adzikro.mydictionary.data.repositories

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.observable
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import smk.adzikro.mydictionary.data.models.Kamus
import smk.adzikro.mydictionary.data.models.KamusDao
import smk.adzikro.mydictionary.ui.adapter.MainPagingSource
import smk.adzikro.mydictionary.utils.config
import javax.inject.Inject

class RepoImpl @Inject constructor(
    private val dao: KamusDao,
    private val context: Context
) : Repo {

    override fun getKamus(kata: String): Flowable<MutableList<Kamus>> {
        if (context.config.isArtinclude)
            return dao.getKamus(kata) else
            return dao.getKamusCari(kata)
    }

    override fun getKamusPaging(
        kata: String
    ): Observable<PagingData<Kamus>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MainPagingSource(dao, kata)
            }
        ).observable
    }

    override fun addKamus(kamus: MutableList<Kamus>) = Completable.fromAction {
        dao.addKamus(kamus)
    }

    override fun getListKata(): Flowable<List<String>> {
        return dao.getListKata()
    }

    override fun updateKamus(kamus: Kamus) = Completable.fromAction {
        dao.updateKamus(kamus)
    }

    override fun getFavorite(): Flowable<MutableList<Kamus>> {
        return dao.getFavorite()
    }

}