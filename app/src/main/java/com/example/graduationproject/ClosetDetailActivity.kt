package com.example.graduationproject

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ClosetDetailActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closet_detail)

        var ccl_intro = intent.getStringExtra("cl_intro").toString()
        var cimg_url = intent.getStringExtra("img_url").toString()
        var ccategory_id = intent.getStringExtra("category_id").toString()
        var cbrand_id = intent.getStringExtra("brand_id").toString()
        var cstyle = intent.getStringExtra("style").toString()


        val closetClIntro = findViewById<TextView>(R.id.closetClIntro)
        val closetImage = findViewById<ImageView>(R.id.closetImage)
        val closetCategory = findViewById<TextView>(R.id.closetCategory)
        val closetBrand = findViewById<TextView>(R.id.closetBrand)
        val closetStyle = findViewById<TextView>(R.id.closetStyle)

        closetClIntro.text = "품명: " + ccl_intro
        closetCategory.text = "카테고리: " + ccategory_id
        closetBrand.text = "브랜드: " + cbrand_id
        closetStyle.text = "스타일: " + cstyle
        Glide.with(this).load(cimg_url).into(closetImage)
    }
}