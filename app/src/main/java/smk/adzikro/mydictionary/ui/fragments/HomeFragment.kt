package smk.adzikro.mydictionary.ui.fragments

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import smk.adzikro.mydictionary.R
import smk.adzikro.mydictionary.data.repositories.KamusInterfaces
import smk.adzikro.mydictionary.databinding.MainFragmentBinding
import smk.adzikro.mydictionary.ui.activities.MainActivity

class HomeFragment : Fragment(), KamusInterfaces {
    private var _binding : MainFragmentBinding ? = null
    private val binding get() = _binding!!
    private val TAG = "HomeFragment"
    private var listKata: List<String> = emptyList()

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
        (activity as MainActivity).viewModel.getListKata()
        (context as MainActivity).viewModel.kataList.observe(viewLifecycleOwner) {
            listKata = it
            (context as MainActivity).binding.menuMain.updateSuggestions(listKata)
        }

        binding.apply {
            val s = resources.openRawResource(R.raw.about).bufferedReader().use {
                it.readText() }
            viewEmpties.tvNoData.text = Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY)
        }
    }
    override fun onDestroyView() {
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

}