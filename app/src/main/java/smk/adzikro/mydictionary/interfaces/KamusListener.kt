package smk.adzikro.mydictionary.interfaces

import smk.adzikro.mydictionary.models.Kamus

interface KamusListener {

    fun onLoading(){}
    fun onSuccess(){}
    fun onError(){}
    fun onNotFound(kata : String){}
    fun onHideLoading(){}
    fun onLoadKamus(kamus : MutableList<Kamus>){}
    fun onLoadSuggest(kamus : MutableList<Kamus>){}
}