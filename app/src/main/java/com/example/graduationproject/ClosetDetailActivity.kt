package com.example.graduationproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.example.graduationproject.dataset.closetData


class ClosetDetailActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closet_detail)

        var cimg_url = intent.getStringExtra("img_url").toString()
        var ccategory_id = intent.getStringExtra("category_id").toString()
        var cstyle = intent.getStringExtra("style").toString()


        val closetImage = findViewById<ImageView>(R.id.closetImage)
        val goRecommendBtn = findViewById<Button>(R.id.goRecommendBtn)
        Log.d("####################tag: ", "$ccategory_id")
        Glide.with(this).load(cimg_url).into(closetImage)

        goRecommendBtn.setOnClickListener{
            Toast.makeText(applicationContext,"조합 보기", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, AddDataActivity::class.java)
            intent.putExtra("img_url", cimg_url)
            intent.putExtra("category_id", ccategory_id)
            intent.putExtra("style", cstyle)
            startActivity(intent)
        }
    }
}