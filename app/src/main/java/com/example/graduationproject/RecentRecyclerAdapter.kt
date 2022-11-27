package com.example.graduationproject

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.dataset.recentData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RecentRecyclerAdapter(
    private val context: Context) : RecyclerView.Adapter<RecentRecyclerAdapter.ItemViewHolder>(){

    var recentList = mutableListOf<recentData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_recent_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(recentList[position])
    }

    override fun getItemCount(): Int {
        return recentList.size
    }
    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val recentViewItem = itemView.findViewById<ImageView>(R.id.recentViewItem)



        fun bind(recentData: recentData){
            Glide.with(itemView).load(recentData.img_url).into(recentViewItem)
            recentViewItem.setOnClickListener{
                Intent(context, ResultDetailActivity::class.java).apply {
                    putExtra("img_url", recentData.img_url)
                    putExtra("category_id", recentData.category_id)
                    putExtra("brand_id", recentData.brand_id)
                    putExtra("style", recentData.style)
//                    putExtra("created_at", recentData.created_at)
                }.run { context.startActivity(this) }

            }
        }
    }
}