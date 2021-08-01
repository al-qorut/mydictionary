package smk.adzikro.mydictionary.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import smk.adzikro.mydictionary.R
import smk.adzikro.mydictionary.models.Kamus

class SugestiAdapter(val context: Context,
val kamus: MutableList<Kamus>) : BaseAdapter() {
    override fun getCount(): Int {
        return kamus.size
    }

    override fun getItem(position: Int): Any {
        return kamus[position]
    }

    override fun getItemId(position: Int): Long {
        return kamus[position]._id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view =inflater.inflate(R.layout.item_sugesti, parent, false)
        val title = view.findViewById<TextView>(R.id.judul)
        title.text = kamus[position].kata
        return view
    }
}