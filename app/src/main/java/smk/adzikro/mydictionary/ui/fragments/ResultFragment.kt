package smk.adzikro.mydictionary.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import smk.adzikro.mydictionary.R
import smk.adzikro.mydictionary.data.models.Kamus
import smk.adzikro.mydictionary.data.repositories.KamusInterfaces
import smk.adzikro.mydictionary.databinding.ResultFragmentBinding
import smk.adzikro.mydictionary.ui.activities.MainActivity
import smk.adzikro.mydictionary.ui.adapter.AdapterFavorite

class ResultFragment : Fragment(), KamusInterfaces {
    private var _binding: ResultFragmentBinding? = null
    private val binding get() = _binding!!
    private val TAG = "ResultFragment"
    private lateinit var adapterKamus: AdapterFavorite
    private var kata = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ResultFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as MainActivity).viewModel.kamusInterfaces = this
        super.onViewCreated(view, savedInstanceState)
        kata = arguments?.getString(KATA_CARI) as String
        Log.e("Di Fragment", "Received search keyword: $kata")
        kata.let {
            (context as MainActivity).viewModel.getData(kata)
        }
        adapterKamus = AdapterFavorite()
        adapterKamus.setCari(kata)
        binding.apply {
            rvResult.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = adapterKamus
                setHasFixedSize(true)
            }
        }
    }

    override fun onDestroyView() {
        adapterKamus.differ.submitList(null)
        super.onDestroyView()
        _binding = null
    }

    override fun onLoading(isLoading: Boolean) {
        isLoading.let {
            (context as MainActivity).isLoading(it)
        }
    }

    override fun onError(message: String) {
        Log.e(TAG, message)
    }

    override fun onLoadKamus(kamus: PagingData<Kamus>) {
      /*  adapterKamus.submitData(lifecycle, kamus)
        adapterKamus.addLoadStateListener { loadState ->
            val isEmpty = loadState.refresh is LoadState.NotLoading && adapterKamus.itemCount == 0
            if (isEmpty) {
                binding.viewEmptiesResult.root.isVisible = true
                binding.viewEmptiesResult.tvNoData.text = getString(R.string.tak_ada_hasil, kata)
                binding.rvResult.isVisible = false
            } else {
                binding.viewEmptiesResult.root.isVisible = false
                binding.rvResult.isVisible = true
            }
        } */
    }

    override fun onLoadResult(kamus: MutableList<Kamus>) {
        adapterKamus.differ.submitList(kamus)
        if (kamus.isEmpty()) {
            binding.viewEmptiesResult.root.isVisible = true
            binding.viewEmptiesResult.tvNoData.text = getString(R.string.tak_ada_hasil, kata)
            binding.rvResult.isVisible = false
        } else {
            binding.viewEmptiesResult.root.isVisible = false
            binding.rvResult.isVisible = true
        }
    }
    companion object {
        val KATA_CARI = "kata_cari"
    }

}