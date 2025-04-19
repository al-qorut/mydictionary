package smk.adzikro.mydictionary.ui.adapter

import android.util.Log
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import smk.adzikro.mydictionary.data.models.Kamus
import smk.adzikro.mydictionary.data.models.KamusDao
import javax.inject.Inject

class MainPagingSource @Inject constructor(
    private val ado : KamusDao,
    private val query: String
) : RxPagingSource<Int, Kamus>() {


    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Kamus>> {
        val position = params.key ?: 1
        var ofset = (position - 1) * 20
        return ado.getKamusPaging(query, 20, ofset) // Pass pageSize and offset
            .subscribeOn(Schedulers.io())
            .map { kamusList ->
                Log.d("SQL_QUERY", "Executing query: SELECT * From kamus Where kata LIKE '%$query%' COLLATE NOCASE or arti LIKE '%$query%' COLLATE NOCASE LIMIT 20 OFFSET $ofset") // Interpolate values for debugging
                toLoadResult(kamusList, position, 20)
            }
            .onErrorReturn {
                Log.e("PAGING_ERROR", "Error loading data: ${it.message}")
                LoadResult.Error(it) }
    }

    private fun toLoadResult(data: List<Kamus>, position: Int, pageSize: Int): LoadResult<Int, Kamus> {
        val prevKey = if (position == 1) null else position - 1
        val nextKey = if (data.size < pageSize) null else position + 1 // Check if less than pageSize items, then no more pages

        return LoadResult.Page(
            data = data,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Kamus>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val page = state.closestPageToPosition(anchorPosition)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }
}
