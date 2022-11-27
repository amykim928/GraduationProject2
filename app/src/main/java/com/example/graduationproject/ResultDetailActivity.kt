package com.example.graduationproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.graduationproject.dataset.recentData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.protobuf.TimestampProto
import java.sql.Timestamp
import java.time.LocalDateTime


class ResultDetailActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_detail)

        val toSearchIntent =Intent(this,MainActivity::class.java)
        toSearchIntent.putExtra("fragment_id",1)
        toSearchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
        toSearchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val data = hashMapOf(
            "img_url" to intent.getStringExtra("img_url").toString(),
            "category_id" to intent.getStringExtra("category_id").toString(),
            "brand_id" to intent.getStringExtra("brand_id").toString(),
            "style" to intent.getStringExtra("style").toString()
        )
//        data.put("created_at", LocalDateTime.now().toString())
//        data.put("created_at", System.currentTimeMillis().toString())

        val db = Firebase.firestore
        db.collection("recentItem").add(data)
            .addOnSuccessListener{
                //Toast.makeText(this, "최근 본 의상에 저장", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // 실패할 경우
                Log.w("ResultDetailActivity", "Error getting documents: $exception")
            }


        var img_url = intent.getStringExtra("img_url").toString()
        var category_id = intent.getStringExtra("category_id").toString()
        var brand_id = intent.getStringExtra("brand_id").toString()
        var style = intent.getStringExtra("style").toString()
        //var cl_intro = intent.getStringExtra("cl_intro").toString()


        val resultImage = findViewById<ImageView>(R.id.resultImage)
        val resultCategory = findViewById<TextView>(R.id.resultCategory)
        val resultBrand = findViewById<TextView>(R.id.resultBrand)
        val resultStyle = findViewById<TextView>(R.id.resultStyle)
        val saveBtn = findViewById<Button>(R.id.saveBtn)

        resultCategory.text = "카테고리: " + category_id
        resultBrand.text = "브랜드: " + brand_id
        resultStyle.text = "스타일: " + style
        Glide.with(this).load(img_url).into(resultImage)


        saveBtn.setOnClickListener { //저장하기 버튼 클릭 시

            val data = hashMapOf(
                "cl_intro" to intent.getStringExtra("cl_intro").toString(),
                "img_url" to intent.getStringExtra("img_url").toString(),
                "category_id" to intent.getStringExtra("category_id").toString(),
//                "brand_id" to intent.getStringExtra("brand_id").toString(),
                "style" to intent.getStringExtra("style").toString()

            )
            val db = Firebase.firestore
            db.collection("closetData").add(data)
                .addOnSuccessListener{
                    Toast.makeText(this, "저장했습니다!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    // 실패할 경우
                    Log.w("ResultDetailActivity", "Error getting documents: $exception")
                }
            startActivity(toSearchIntent)
            finish()
        }
    }

}