package smk.adzikro.mydictionary.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import smk.adzikro.mydictionary.interfaces.KamusListener
import smk.adzikro.mydictionary.models.Kamus
import smk.adzikro.mydictionary.repositories.KamusRepo

class KamusViewModel(
    private val repo: KamusRepo
    ):ViewModel() {
    private val disposables = CompositeDisposable()
    var kamusListener : KamusListener ?= null

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun addKamus(kamus:MutableList<Kamus>){
       kamusListener?.onLoading()
       val disposable = repo.addKamus(kamus)
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe({
               kamusListener?.onSuccess()
           },{
              kamusListener?.onError()
           })
        disposables.add(disposable)
    }

   fun getKamus(kata:String){
       kamusListener?.onLoading()
       val disposable = repo.getKamus(kata)
           ?.subscribeOn(Schedulers.io())
           ?.observeOn(AndroidSchedulers.mainThread())
           ?.subscribe({
               kamusListener?.onLoadKamus(it)
           },{
               kamusListener?.onNotFound(it.toString())
           })
       disposables.add(disposable!!)
   }

    fun getSuggest(kata:String){
        kamusListener?.onLoading()
        val disposable = repo.getSuggest(kata)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                kamusListener?.onLoadSuggest(it)
            },{
                kamusListener?.onNotFound(it.toString())
            })
        disposables.add(disposable!!)
    }
}