package smk.adzikro.mydictionary.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import smk.adzikro.mydictionary.repositories.KamusRepo

class KamusViewModelFactory(
    private val repo : KamusRepo
    ):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return KamusViewModel(repo) as T
    }
}