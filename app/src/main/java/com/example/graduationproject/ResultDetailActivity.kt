package com.example.graduationproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ResultDetailActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_detail)

        var img_url = intent.getStringExtra("img_url").toString()
        var category_id = intent.getStringExtra("category_id").toString()
        var brand_id = intent.getStringExtra("brand_id").toString()
        var style = intent.getStringExtra("style").toString()

        val resultImage = findViewById<ImageView>(R.id.resultImage)
        val resultCategory = findViewById<TextView>(R.id.resultCategory)
        val resultBrand = findViewById<TextView>(R.id.resultBrand)
        val resultStyle = findViewById<TextView>(R.id.resultStyle)
        val saveBtn = findViewById<Button>(R.id.saveBtn)
        val backBtn=findViewById<Button>(R.id.backBtn)

        resultCategory.text = "카테고리: " + category_id
        resultBrand.text = "브랜드: " + brand_id
        resultStyle.text = "스타일: " + style
        Glide.with(this).load(img_url).into(resultImage)

        saveBtn.setOnClickListener { //저장하기 버튼 클릭 시

        }
        backBtn.setOnClickListener { //저장하기 버튼 클릭 시
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra("fragment_id",1)
            startActivity(intent)
            finish()
        }
    }

}