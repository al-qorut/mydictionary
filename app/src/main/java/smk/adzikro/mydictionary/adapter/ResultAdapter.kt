package smk.adzikro.mydictionary.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import smk.adzikro.mydictionary.R
import smk.adzikro.mydictionary.models.Kamus
import smk.adzikro.mydictionary.tools.*

class ResultAdapter(val context: Context,
private val kamus: MutableList<Kamus>):
    RecyclerView.Adapter<ResultAdapter.HolderKamus>(){
    val TAG = "ResultAdapter"
    val fontFace = ResourcesCompat.getFont(context, R.font.amiri)

    inner class HolderKamus(view: View):RecyclerView.ViewHolder(view) {
        val textCari = view.findViewById<TextView>(R.id.kolomKata)
        val textArti = view.findViewById<TextView>(R.id.kolomArtinya)

        fun bind(dic: Kamus){
            textCari.text = dic.kata
            if (isArabic(dic.arti)){
                //val fontFace = ResourcesCompat.getFont(context, R.font.amiri)
                textArti.textSize = 20f
                textArti.typeface = Typeface.create(fontFace, Typeface.NORMAL)
            }else
                textArti.textSize = 10f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                textArti.text= Html.fromHtml(dic.arti, 0)
            } else {
                textArti.text= Html.fromHtml(dic.arti)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderKamus{
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_result, parent, false)
                return HolderKamus(view)
    }

    override fun onBindViewHolder(holder: HolderKamus, position: Int) {
            holder.bind(kamus[position])
            val img = holder.itemView.findViewById<ImageView>(R.id.id_copy)
            holder.itemView.setOnClickListener {
                AlqDialog.build(context as Activity)
                    .icon(R.drawable.ic_congrts, true)
                    .title(kamus[position].kata)
                    .body(kamus[position].arti, Typeface.create(fontFace, Typeface.NORMAL))
                    .onPositive(context.getString(R.string.done))
            }
            img.setOnClickListener {
                setClipboard(context, kamus[position].kata + "\n" + kamus[position].arti)
            }
    }
    override fun getItemCount(): Int = kamus.size


}