package com.example.graduationproject

import android.annotation.SuppressLint
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

    private var brand_name = listOf<String>(
        "","블랙야크", "유니클로", "abc마트", "", "X", "", "와릿이즌", "예일", "클로티"
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_detail)

        val toSearchIntent =Intent(this, ClosetFragment::class.java)
        toSearchIntent.putExtra("fragment_id",1)
        toSearchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        toSearchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        //recentItem 컬렉션에 넣을 데이터를 hashmap에 담아 recyclerview에 넣어줌
        val data = hashMapOf(
            "img_url" to intent.getStringExtra("img_url").toString(),
            "img_base64 " to "",
            "category_id" to intent.getStringExtra("category_id").toString(),
            "brand_id" to intent.getStringExtra("brand_id").toString(),
            "style" to intent.getStringExtra("style").toString()
        )

        //hashmap의 key와 value는 다른 타입일 수 있지만, key끼리/value끼리는 같은 타입이어야만 함
        //최신순으로 정렬하고자 날짜 비교를 위해 현재 날짜및시간을 나타내는 것을 숫자로 받으려고 했으나 불가능했고,
        //orderby()라는 함수를 사용하였지만, String타입이 sorting되었음
        //그래서 10000000000000에서 created_at으로 받은 숫자를 빼서 sorting 하는 방법을 사용함
//        data.put("created_at", LocalDateTime.now().toString())
        val a = (10000000000000 - System.currentTimeMillis())
        data.put("created_at", a.toString())
        Log.d("################System.currentTimeMillis(): ", "${a.toLong()}")
        Log.d("################System.currentTimeMillis().toString(): ",
            (System.currentTimeMillis() * (-1)).toString()
        )

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

        resultCategory.text = "카테고리: $category_id"
        resultBrand.text = "브랜드: " + brand_id
        resultStyle.text = "스타일: " + style
        Glide.with(this).load(img_url).into(resultImage)


        saveBtn.setOnClickListener { //저장하기 버튼 클릭 시

            val data = hashMapOf(
                "cl_intro" to intent.getStringExtra("cl_intro").toString(),
                "img_url" to intent.getStringExtra("img_url").toString(),
                "category_id" to intent.getStringExtra("category_id").toString(),
                "style" to intent.getStringExtra("style").toString(),
                "img_base64 " to "",

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