package smk.adzikro.mydictionary.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import smk.adzikro.mydictionary.data.models.Kamus
import smk.adzikro.mydictionary.data.repositories.KamusInterfaces
import smk.adzikro.mydictionary.data.repositories.Repo
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {
    var kamusInterfaces: KamusInterfaces? = null
    private var disposables = CompositeDisposable()
    private val TAG = "MainViewModel"
    val kataList: MutableLiveData<List<String>> = MutableLiveData()

    fun getFavorite() {
        kamusInterfaces?.onLoading(true)
        val disposable = repo.getFavorite()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    kamusInterfaces?.onLoadFavorite(it)
                    kamusInterfaces?.onLoading(false)
                }, {
                    kamusInterfaces?.onError(it.message.toString())
                    kamusInterfaces?.onLoading(false)
                }
            )
        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun getData(kata: String) {
        kamusInterfaces?.onLoading(true)
        val disposable = repo.getKamus(kata)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    kamusInterfaces?.onLoadResult(it)
                    kamusInterfaces?.onLoading(false)
                },{
                    kamusInterfaces?.onLoading(false)
                    kamusInterfaces?.onError(it.message.toString())
                }
            )
        disposables.add(disposable)
    }


    fun addKamus(kamus: MutableList<Kamus>) {
        kamusInterfaces?.onLoading(true)
        val disposable = repo.addKamus(kamus)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe( {
                kamusInterfaces?.onLoading(false)
                Log.e(TAG, "addKamus: sukses")

            },{
                kamusInterfaces?.onLoading(false)
                Log.e(TAG, "addKamus: ${it.message}")
            })
        disposables.add(disposable)
    }
    fun udpateKamus(kamus: Kamus) {
        kamusInterfaces?.onLoading(true)
        val disposable = repo.updateKamus(kamus)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe( {
                kamusInterfaces?.onLoading(false)
                Log.e(TAG, "updateKamus: sukses")

            },{
                kamusInterfaces?.onLoading(false)
                Log.e(TAG, "updateKamus: ${it.message}")
            })
        disposables.add(disposable)
    }

    fun getListKata() {
        if (kataList.value == null) {
            kamusInterfaces?.onLoading(true)
            val disposable = repo.getListKata()
                .take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    kataList.value = it
                    kamusInterfaces?.onLoading(false)
                    Log.e(TAG, "getListKata: sukses")

                }, {
                    kamusInterfaces?.onLoading(false)
                    Log.e(TAG, "getListKata: ${it.message}")
                })
            disposables.add(disposable)
        }
    }
}