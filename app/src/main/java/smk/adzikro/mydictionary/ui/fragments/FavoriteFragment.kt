package smk.adzikro.mydictionary.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import smk.adzikro.mydictionary.R
import smk.adzikro.mydictionary.data.models.Kamus
import smk.adzikro.mydictionary.data.repositories.KamusInterfaces
import smk.adzikro.mydictionary.databinding.MainFragmentBinding
import smk.adzikro.mydictionary.ui.activities.MainActivity
import smk.adzikro.mydictionary.ui.adapter.AdapterFavorite
import smk.adzikro.mydictionary.utils.toast

class FavoriteFragment: Fragment(), KamusInterfaces {
    private var _binding : MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val TAG = "FavoriteFragment"
    private lateinit var adapterFavorite: AdapterFavorite

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).viewModel.kamusInterfaces = this
        (context as MainActivity).getFavorite()
        adapterFavorite = AdapterFavorite()
        binding.apply {
            rvHome.apply {
                adapter = adapterFavorite
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapterFavorite.differ.submitList(emptyList())
    }


    override fun onLoading(isLoading: Boolean) {
        isLoading.let {
            (context as MainActivity).isLoading(it)
        }
    }

    override fun onLoadFavorite(kamus: MutableList<Kamus>) {
        adapterFavorite.differ.submitList(kamus)
        if (kamus.isEmpty()) {
            binding.viewEmpties.root.isVisible = true
            binding.viewEmpties.tvNoData.text = getString(R.string.tak_ada_favorite)
            binding.rvHome.isVisible = false
        } else {
            binding.viewEmpties.root.isVisible = false
            binding.rvHome.isVisible = true
        }
    }

    override fun onError(message: String) {
        toast(requireContext(), message)
    }


}