package smk.adzikro.mydictionary.data.repositories

import androidx.paging.PagingData
import smk.adzikro.mydictionary.data.models.Kamus

interface KamusInterfaces {

    fun onLoading(isLoading: Boolean)
    fun onLoadFavorite(kamus: MutableList<Kamus>){}
    fun onError(message: String)
    fun onLoadKamus(kamus: PagingData<Kamus>){}
    fun onLoadResult(kamus: MutableList<Kamus>){}
}