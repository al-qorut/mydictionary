package smk.adzikro.mydictionary.ui.adapter

import android.annotation.SuppressLint
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import smk.adzikro.mydictionary.R
import smk.adzikro.mydictionary.data.models.Kamus
import smk.adzikro.mydictionary.databinding.ItemResultBinding
import smk.adzikro.mydictionary.ui.activities.MainActivity
import smk.adzikro.mydictionary.utils.captureViewAsBitmap
import smk.adzikro.mydictionary.utils.getFont
import smk.adzikro.mydictionary.utils.getFontSize
import smk.adzikro.mydictionary.utils.getFontSizeAr
import smk.adzikro.mydictionary.utils.isArabic
import smk.adzikro.mydictionary.utils.setClipboard
import smk.adzikro.mydictionary.utils.setTextKata
import smk.adzikro.mydictionary.utils.shareImage

class AdapterKamus(

) : PagingDataAdapter<Kamus, AdapterKamus.ViewHolder>(DIFF_CALLBACK)
     {
    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Kamus>() {
            override fun areItemsTheSame(oldItem: Kamus, newItem: Kamus): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Kamus, newItem: Kamus): Boolean {
                return oldItem == newItem
            }

        }
    }
    class ViewHolder(
        private val binding: ItemResultBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(kamus: Kamus) {
            binding.apply {

                if(isArabic(kamus.kata)){
                    textKata.textDirection = View.TEXT_DIRECTION_RTL
                    textKata.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
                    textKata.textSize = getFontSizeAr(binding.root.context)
                    textKata.typeface = getFont(binding.root.context)
                }else{
                    textKata.textDirection = View.TEXT_DIRECTION_LTR
                    textKata.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                    textKata.textSize = getFontSize(binding.root.context)
                }
                val cari = (itemView.context as MainActivity).kata
                textKata.text = setTextKata(cari, kamus.kata)
                if(isArabic(kamus.arti)){
                    textArti.textDirection = View.TEXT_DIRECTION_RTL
                    textArti.textAlignment = View.TEXT_ALIGNMENT_VIEW_END
                    textArti.textSize = getFontSizeAr(binding.root.context)
                    textArti.typeface = getFont(binding.root.context)
                }else{
                    textArti.textDirection = View.TEXT_DIRECTION_LTR
                    textArti.textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                    textArti.textSize = getFontSize(binding.root.context)
                }
                if(!kamus.root.isEmpty()){
                    textRoot.visibility = View.VISIBLE
                    textRoot.textDirection = View.TEXT_DIRECTION_RTL
                    textRoot.textSize = getFontSizeAr(binding.root.context)
                    textRoot.text = setTextKata(cari, kamus.root)
                }else{
                    textRoot.visibility = View.GONE
                }
                if(!kamus.source.isEmpty()){
                    textSource.visibility = View.VISIBLE
                    textSource.text = binding.root.context.getString(R.string.source, kamus.source)
                }else{
                    textSource.visibility = View.GONE
                }
                textArti.text = setTextKata(cari, kamus.arti)
                textArti.text = Html.fromHtml(kamus.arti, Html.FROM_HTML_MODE_LEGACY)
                if(kamus.favorite == 1){
                    imageFavorite.setImageResource(R.drawable.ic_favorite_black_24dp)
                }else{
                    imageFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                }

                imageCopy.setOnClickListener {
                    setClipboard(binding.root.context, kamus.arti)
                }
                imageShare.setOnClickListener {
                    val x = captureViewAsBitmap(binding.root)
                    shareImage(binding.root.context, x)
                }
                imageFavorite.setOnClickListener {
                    val favo = if(kamus.favorite == 0) 1 else 0
                    kamus.favorite = favo
                    (itemView.context as MainActivity).viewModel.udpateKamus(kamus)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position) as Kamus)
    }



}