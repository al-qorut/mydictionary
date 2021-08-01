package smk.adzikro.mydictionary

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import smk.adzikro.mydictionary.adapter.ResultAdapter
import smk.adzikro.mydictionary.adapter.SugestiAdapter
import smk.adzikro.mydictionary.databinding.ActivityMainBinding
import smk.adzikro.mydictionary.interfaces.KamusListener
import smk.adzikro.mydictionary.models.Kamus
import smk.adzikro.mydictionary.repositories.DataSource
import smk.adzikro.mydictionary.repositories.KamusRepo
import smk.adzikro.mydictionary.tools.*
import smk.adzikro.mydictionary.viewmodels.KamusViewModel
import smk.adzikro.mydictionary.viewmodels.KamusViewModelFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/*
 git init
 git remote add origin https://github.com/al-qorut/mydictionary.git
 git pull origin master --allow-unrelated-histories
 git add .
 git commit -m “pesan”
 git push origin master
*/
class MainActivity : AppCompatActivity(), KamusListener,
    SearchView.OnQueryTextListener{
    private lateinit var dataSource: DataSource
    private lateinit var repo: KamusRepo
    private lateinit var factory : KamusViewModelFactory
    private lateinit var viewModel: KamusViewModel
    private lateinit var binding : ActivityMainBinding
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        dataSource = DataSource(this)
        repo = KamusRepo(dataSource)
        factory = KamusViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory).get(KamusViewModel::class.java)
        binding.viewmodel = viewModel
        viewModel.kamusListener = this
        binding.cari.setOnQueryTextListener(this)
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        binding.adView.adListener = object : AdListener() {
             override fun onAdLoaded() {
                super.onAdLoaded()
                Log.e(TAG, "onAdLoaded")
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                Log.e(TAG, "onAdFailedToLoad ${p0.message}")
            }

            override fun onAdClosed() {
                super.onAdClosed()
                Log.e(TAG, "onAdClosed ")
            }

            override fun onAdOpened() {
                super.onAdOpened()
                Log.e(TAG, "onAdOpened ")
            }

            override fun onAdClicked() {
                super.onAdClicked()
                Log.e(TAG, "onAdCliked ")
            }

            override fun onAdImpression() {
                super.onAdImpression()
                Log.e(TAG, "onAdImpession ")
            }
        }
        binding.menuhandap.setOnItemSelectedListener{
            when(it.itemId){
                R.id.action_about -> About()
                R.id.action_rate ->  Rate(this)
                R.id.action_share -> Share(this)
                R.id.action_settings ->{}
            }
            false
        }
    }

    override fun onStart() {
        super.onStart()
        if (isPertama(this)) {
            extractData()
        }
    }

    override fun onError() {
        super.onError()
        binding.progressBar.visibility = View.GONE
    }

    override fun onLoading() {
        super.onLoading()
        binding.progressBar.visibility = View.VISIBLE
       // Log.e(TAG, "onLoading")
    }

    override fun onHideLoading() {
        super.onHideLoading()
        binding.progressBar.visibility = View.GONE
        Log.e(TAG, "onHide")
    }

    override fun onSuccess() {
        super.onSuccess()
        binding.progressBar.visibility = View.GONE
        binding.cari.visibility = View.VISIBLE
        setPertama(false,this)
        //Log.e(TAG, "Sukseess... add kamus")
        Toast.makeText(this, " Data telah siap...", Toast.LENGTH_SHORT).show()
    }

    override fun onLoadKamus(kamus: MutableList<Kamus>) {
        super.onLoadKamus(kamus)
        binding.progressBar.visibility = View.GONE
        if(kamus.size==0 || kamus.isNullOrEmpty()){
            binding.sugesti.visibility = View.GONE
            binding.listHasil.visibility = View.GONE
        }else{
            binding.sugesti.visibility = View.GONE
            val manager = LinearLayoutManager(this)
            binding.listHasil.layoutManager = manager
            val adapter = ResultAdapter(this, kamus)
            binding.listHasil.adapter = adapter
            binding.listHasil.visibility = View.VISIBLE
        }
    }

    override fun onLoadSuggest(data: MutableList<Kamus>) {
        super.onLoadSuggest(data)
        binding.progressBar.visibility = View.GONE
        //Log.e(TAG, "${data.size}")
        if(data.size==0 || data.isNullOrEmpty()){
            binding.sugesti.visibility = View.GONE
        }else{
            val suges = SugestiAdapter(this, data)
            binding.sugesti.adapter = suges
            binding.sugesti.visibility = View.VISIBLE
            binding.sugesti.setOnItemClickListener({ parent, view, position, id ->
                val t = view.findViewById<TextView>(R.id.judul)
                viewModel.getKamus(t.text.toString())
                binding.sugesti.visibility = View.GONE
            })
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query !=null && query.length>=2 && query.isNotEmpty() && query.isNotBlank()) {
            viewModel.getKamus(query)
            binding.sugesti.visibility = View.GONE
        }
        return true
    }

    override fun onNotFound(kata: String) {
        super.onNotFound(kata)
        Toast.makeText(this, kata+" tidak ada", Toast.LENGTH_SHORT).show()
    }
    override fun onQueryTextChange(newText: String?): Boolean {
        binding.listHasil.visibility = View.GONE
        if (newText != null) {
            if(newText.length>=2 && newText.isNotEmpty() && newText.isNotBlank()) {
                viewModel.getSuggest(newText)
            }
        }
        return true
    }
    /* =============================
        Extract Data
     ===============================*/

    private fun extractData(){
        binding.cari.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        Toast.makeText(this, getString(R.string.extract_data), Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).post { copyKamus() }
    }
    private fun copyKamus(){
        val dic : MutableList<Kamus> = mutableListOf()
        var i = 0
        // Kamus Ind - Ing
        val inputStream  = resources.openRawResource(R.raw.gkamus_id)
        val list = BufferedReader(InputStreamReader(inputStream)).lineSequence().iterator()

        try {
            while (list.hasNext()) {
                val line = list.next()
                val kata = line.split("\t")
                if (kata.size<=1) continue
                // val arti = kata.joinToString()
                val kamus = Kamus(i, kata[0], kata[1])
                dic.add(kamus)
                i++
            }
        }catch (e: IOException) {
            e.printStackTrace()
        }

        // eng - ind
        val ins  = resources.openRawResource(R.raw.gkamus_en)
        val br = BufferedReader(InputStreamReader(ins)).lineSequence().iterator()
        try {
            while (br.hasNext()) {
                val line = br.next()
                val kata = line.split("\t")
                if (kata.size<2) continue
                val kamus = Kamus(i, kata[0], kata[1])
                dic.add(kamus)
                i++
            }
        }catch (e: IOException) {
            e.printStackTrace()
        }

        // Kamus Ind - Ind
        val insm  = resources.openRawResource(R.raw.kbbi_data)
        val bfrm = BufferedReader(InputStreamReader(insm)).lineSequence().iterator()
        try {
            while (bfrm.hasNext()) {
                val line = bfrm.next()
                val kata = line.split(",")
                if (kata.size<2) continue
                val kamus = Kamus(i, kata[0], getHtml(kata[1]))
                dic.add(kamus)
                i++
            }
        }catch (e: IOException) {
            e.printStackTrace()
        }

        // Kamus Ing - Ing
        val inputStream1  = resources.openRawResource(R.raw.en_en)
        val bufferedReader1 = BufferedReader(InputStreamReader(inputStream1)).lineSequence().iterator()
        try {
            while (bufferedReader1.hasNext()) {
                val line = bufferedReader1.next()
                val kata = line.split(" ")
                if (kata.size<2) continue
                val arti = kata.joinToString()
                val kamus = Kamus(i, kata[0], arti)
                dic.add(kamus)
                i++
            }
        }catch (e: IOException) {
            e.printStackTrace()
        }
        // Kamus ar - in
        val inputArin  = resources.openRawResource(R.raw.arin)
        val bufferedArin = BufferedReader(InputStreamReader(inputArin)).lineSequence().iterator()
        try {
            while (bufferedArin.hasNext()) {
                val line = bufferedArin.next()
                val kata = line.split(";")
                if (kata.size<2) continue
                val kamus = Kamus(i, kata[0], kata[1])
                dic.add(kamus)
                i++
            }
        }catch (e: IOException) {
            e.printStackTrace()
        }
    // Kamus ar - ar
        val inputArar  = resources.openRawResource(R.raw.arar)
        val bufferedArar = BufferedReader(InputStreamReader(inputArar)).lineSequence().iterator()
        try {
            while (bufferedArar.hasNext()) {
                val line = bufferedArar.next()
                val kata = line.split(";")
                if (kata.size<2) continue
                val kamus = Kamus(i, kata[0], kata[1])
                dic.add(kamus)
                i++
            }
        }catch (e: IOException) {
            e.printStackTrace()
        }
        viewModel.addKamus(dic)
    }
    /*=================================================================*/
    //Kumpulan fungsi-fungsi
    //================================================================
    private fun About(){
        AlqDialog.build(this)
            .icon(R.drawable.ic_assignment_ind_black_24dp, true)
            .title(String.format(getString(R.string.title_about), applicationName(this), getVersi(this)))
            .body(getString(R.string.body_about))
            .onPositive(getString(R.string.done))
    }
    private fun Aboutx() {
        val alert= AlertDialog.Builder(this)
        alert.setTitle(String.format(getString(R.string.title_about), applicationName(this), getVersi(this)))
        alert.setMessage(getString(R.string.body_about))
            .setCancelable(false)
            .setPositiveButton("Ok",
                { dialog, which -> dialog.cancel() }).show()
    }


}
