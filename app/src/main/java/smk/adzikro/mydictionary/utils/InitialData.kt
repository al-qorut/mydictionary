package smk.adzikro.mydictionary.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.Log
import smk.adzikro.mydictionary.R
import smk.adzikro.mydictionary.data.models.Kamus
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object InitialData {
    private val TAG = "InitialData"

    fun getIndEng(context: Context) : MutableList<Kamus> {
        val dic : MutableList<Kamus> = mutableListOf()
        var i = 0
        val inputStream  = context.resources.openRawResource(R.raw.gkamus_id)
        val list = BufferedReader(InputStreamReader(inputStream)).lineSequence().iterator()

        try {
            while (list.hasNext()) {
                val line = list.next()
                val kata = line.split(";")
                if (kata.size<=1) continue
                val kamus = Kamus(0, kata[0], kata[1], source = "gkamus.sourceforge.net")
                dic.add(kamus)
                i++
            }
        }catch (e: IOException) {
           Log.e(TAG,"getIndEng: ${e.message}")
        }
        Log.e(TAG,"getIndEng: sukses ${dic.size}")
        return dic
    }
    fun getEngInd(context: Context) : MutableList<Kamus> {
        val dic : MutableList<Kamus> = mutableListOf()
        var i = 0
        val ins  = context.resources.openRawResource(R.raw.gkamus_en)
        val br = BufferedReader(InputStreamReader(ins)).lineSequence().iterator()
        try {
            while (br.hasNext()) {
                val line = br.next()
                val kata = line.split(";")
                if (kata.size<2) continue
                val kamus = Kamus(0,kata[0], kata[1], source = "gkamus.sourceforge.net")
                dic.add(kamus)
                i++
            }
        }catch (e: IOException) {
            Log.e(TAG,"getEngInd: ${e.message}")
        }
        Log.e(TAG,"getEngInd: sukses ${dic.size}")
        return dic
    }
    fun getEngEng(context: Context) : MutableList<Kamus> {
        val dic: MutableList<Kamus> = mutableListOf()
        val inputStream1  = context.resources.openRawResource(R.raw.en_en)
        val bufferedReader1 = BufferedReader(InputStreamReader(inputStream1)).lineSequence().iterator()
        var i = 0
        try {
            while (bufferedReader1.hasNext()) {
                val line = bufferedReader1.next()
                val kata = line.split("\t")
                if (kata.size<2) continue
                val arti = kata.joinToString()
                val kamus = Kamus(0,kata[0], kata[1], source = "dict.org")
                dic.add(kamus)
                i++
            }
        }catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG,"getEngEng: ${e.message}")
        }
        Log.e(TAG,"getEngEng: sukses ${dic.size}")
        return dic
    }
    fun getArIn(context: Context) : MutableList<Kamus> {
        val dic: MutableList<Kamus> = mutableListOf()
        val inputArin  = context.resources.openRawResource(R.raw.arin)
        val bufferedArin = BufferedReader(InputStreamReader(inputArin)).lineSequence().iterator()
        var i = 0
        try {
            while (bufferedArin.hasNext()) {
                val line = bufferedArin.next()
                val kata = line.split(";")
                if (kata.size<2) continue
                val kamus = Kamus(0, kata[0], kata[1], source = "unknow")
                dic.add(kamus)
                i++
            }
        }catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG,"getArIn: ${e.message}")
        }
        Log.e(TAG,"getArIn: sukses ${dic.size}")
        return dic
    }
    fun getArAr(context: Context) : MutableList<Kamus> {
        val dic: MutableList<Kamus> = mutableListOf()
        val inputArar  = context.resources.openRawResource(R.raw.arar)
        val bufferedArar = BufferedReader(InputStreamReader(inputArar)).lineSequence().iterator()
        var i = 0
        try {
            while (bufferedArar.hasNext()) {
                val line = bufferedArar.next()
                val kata = line.split(";")
                if (kata.size<2) continue
                val kamus = Kamus(0, kata[0], kata[1], source = "Al-Kamus")
                dic.add(kamus)
                i++
            }
        }catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG,"getArAr: ${e.message}")
        }
        Log.e(TAG,"getArAr: sukses ${dic.size}")
        return dic
    }
    fun getIndInd(context: Context) : MutableList<Kamus> {
        val dic: MutableList<Kamus> = mutableListOf()
        val insm  = context.resources.openRawResource(R.raw.kbbi_data)
        val bfrm = BufferedReader(InputStreamReader(insm)).lineSequence().iterator()
        var i = 0
        try {
            while (bfrm.hasNext()) {
                val line = bfrm.next()
                val kata = line.split(",")
                if (kata.size<2) continue
                val kamus = Kamus(id=0, kata=kata[0], arti = getHtml(kata[1]), source = "KBBI VI")
                dic.add(kamus)
                i++
            }
        }catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG,"getIndInd: ${e.message}")
        }
        Log.e(TAG,"getIndInd: sukses ${dic.size}")
        return dic
    }
    fun getHtml(htmlBody: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(htmlBody, 0).toString()
        else
            Html.fromHtml(htmlBody).toString()
    }
    fun getSourceKamus() : HashMap<Int, String> {
        return hashMapOf<Int, String>().apply {
            put(R.raw.ain, "Ain")
            put(R.raw.algarus1,"")
            put(R.raw.algarus2,"")
            put(R.raw.algarus3,"")
            put(R.raw.algarus4,"")
            put(R.raw.algarus5,"")
            put(R.raw.algarus6,"")
            put(R.raw.algarus7,"")
            put(R.raw.algarus8,"")
            put(R.raw.algarus9,"")
            put(R.raw.algarus10,"")
            put(R.raw.algarus11,"")
            put(R.raw.algarus12,"")
            put(R.raw.algarus13,"")
            put(R.raw.algarus14,"")
            put(R.raw.algarus15,"")
            put(R.raw.algarus16,"")
            put(R.raw.algarus17,"")
            put(R.raw.algarus18,"")
            put(R.raw.algarus19,"")
            put(R.raw.almaghreb,"")
            put(R.raw.almesbah,"")
            put(R.raw.almuheet_1,"")
            put(R.raw.almuheet_2,"")
            put(R.raw.alsehah1,"")
            put(R.raw.alsehah2,"")
            put(R.raw.alsehah3,"")
            put(R.raw.alsehah4,"")
            put(R.raw.alsehah5,"")
            put(R.raw.alsehah6,"")
            put(R.raw.alsehah7,"")
            put(R.raw.alsehah8,"")
            put(R.raw.alsehah9,"")
            put(R.raw.lesan_1,"")
            put(R.raw.lesan_2,"")
            put(R.raw.lesan_3,"")
            put(R.raw.lesan_4,"")
            put(R.raw.lesan_5,"")
            put(R.raw.lesan_6,"")
            put(R.raw.lesan_7,"")
            put(R.raw.lesan_8,"")
            put(R.raw.lesan_91,"")
            put(R.raw.lesan_92,"")
            put(R.raw.lesan_93,"")
            put(R.raw.lesan_94,"")
            put(R.raw.quraan,"")
            put(R.raw.asas,"")
        }
    }
    val LIST_KAMUS = arrayOf(
        R.raw.ain,
        R.raw.algarus1,
        R.raw.algarus2,
        R.raw.algarus3,
        R.raw.algarus4,
        R.raw.algarus5,
        R.raw.algarus6,
        R.raw.algarus7,
        R.raw.algarus8,
        R.raw.algarus9,
        R.raw.algarus10,
        R.raw.algarus11,
        R.raw.algarus12,
        R.raw.algarus13,
        R.raw.algarus14,
        R.raw.algarus15,
        R.raw.algarus16,
        R.raw.algarus17,
        R.raw.algarus18,
        R.raw.algarus19,
        R.raw.almaghreb,
        R.raw.almesbah,
        R.raw.almuheet_1,
        R.raw.almuheet_2,
        R.raw.alsehah1,
        R.raw.alsehah2,
        R.raw.alsehah3,
        R.raw.alsehah4,
        R.raw.alsehah5,
        R.raw.alsehah6,
        R.raw.alsehah7,
        R.raw.alsehah8,
        R.raw.alsehah9,
        R.raw.lesan_1,
        R.raw.lesan_2,
        R.raw.lesan_3,
        R.raw.lesan_4,
        R.raw.lesan_5,
        R.raw.lesan_6,
        R.raw.lesan_7,
        R.raw.lesan_8,
        R.raw.lesan_91,
        R.raw.lesan_92,
        R.raw.lesan_93,
        R.raw.lesan_94,
        R.raw.quraan,
        R.raw.asas
    )
    fun getKamusArab(idkamus : Int, context: Context) : MutableList<Kamus> {
        val dic : MutableList<Kamus> = mutableListOf()
        var i = 0
        val inputStream  = context.resources.openRawResource(idkamus)
        val list = BufferedReader(InputStreamReader(inputStream)).lineSequence().iterator()

        try {
            while (list.hasNext()) {
                val line = list.next()
                val kata = line.split(";")
                if (kata.size<=1) continue
                val kamus = Kamus(0, kata[0], kata[5], root = kata[2], source = "https://data.mendeley.com/")
                dic.add(kamus)
                i++
            }
        }catch (e: IOException) {
            Log.e(TAG,"getKamusArab: ${e.message}")
        }
        Log.e(TAG,"getKamusArab: sukses ${dic.size} ${idkamus}")
        return dic
    }
}