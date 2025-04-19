package smk.adzikro.mydictionary.ui.fragments

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import smk.adzikro.mydictionary.BuildConfig
import smk.adzikro.mydictionary.R
import smk.adzikro.mydictionary.databinding.InfoFragmentBinding

class InfoFragment : Fragment() {
    private var _binding : InfoFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InfoFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val about = resources.openRawResource(R.raw.about).bufferedReader().use {
                it.readText() }
            textAbout.text = Html.fromHtml(about, Html.FROM_HTML_MODE_LEGACY)


            appVersion.text = "Version "+ BuildConfig.VERSION_NAME
            val s = resources.openRawResource(R.raw.ppdd).bufferedReader().use {
                it.readText() }
            textPprj.text = Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}