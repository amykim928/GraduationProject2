package com.example.graduationproject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.dataset.clothData

class RecycleViewAdapter(
    private val context: Context) : RecyclerView.Adapter<RecycleViewAdapter.ItemViewHolder>(){

    var clothList = mutableListOf<clothData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_result_row, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(clothList[position])
    }

    override fun getItemCount(): Int {
        return clothList.size
    }

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val clothImage = itemView.findViewById<ImageView>(R.id.clothImage)
        private val clothImageText = itemView.findViewById<TextView>(R.id.clothImageText)

        fun bind(clothData: clothData){
            clothImageText.text = clothData.cl_intro
            Glide.with(itemView).load(clothData.img_url).into(clothImage)
            clothImage.setOnClickListener{
                Intent(context, ResultDetailActivity::class.java).apply {
                    putExtra("img_url", clothData.img_url)
                    putExtra("category_id", clothData.category_id)
                    putExtra("brand_id", clothData.brand_id.toString())
                    putExtra("style", clothData.style)
                }.run { context.startActivity(this) }
            }
        }
    }
}