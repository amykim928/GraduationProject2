package com.example.graduationproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private var clothList = mutableListOf<clothData>()
lateinit var recycleViewAdapter: RecycleViewAdapter

class SearchResultActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        var keyword = intent.getStringExtra("keyword").toString()
        val keywordResultTitle = findViewById<TextView>(R.id.keywordResultTitle)
        keywordResultTitle.text = keyword + "에 대한 결과입니다."

        val keywordText = findViewById<TextView>(R.id.keywordText)
        keywordText.text = keyword
        val keywordBtn = findViewById<ImageButton>(R.id.keywordBtn)
        keywordBtn.setOnClickListener {
            val keywordStr = keywordText.text.toString()
            keyword = keywordStr
        }

        val clothResultLayout = findViewById<RecyclerView>(R.id.clothResultLayout)
        clothResultLayout.addItemDecoration(VerticalItemDecorator(20))
        clothResultLayout.addItemDecoration(HorizontalItemDecorator(20))
        recycleViewAdapter = RecycleViewAdapter(this)
        clothResultLayout.adapter = recycleViewAdapter

        val db = Firebase.firestore
        db.collection("clothData")
            .whereEqualTo("category_id", keyword)
            .get()
            .addOnSuccessListener { result ->
                clothList.clear()
                for(doc in result) {
                    Log.d("tag: ", "${doc.id} => ${doc.data}")
                    clothList.add(
                        clothData(doc.data.get("brand_id") as Long,
                            doc.data.get("category_id") as String,
                            doc.data.get("cl_intro") as String,
                            doc.data.get("cl_pd_num") as String,
                            doc.data.get("color") as String,
                            doc.data.get("id") as Long,
                            doc.data.get("img_url") as String,
                            doc.data.get("style") as String)
                    )
                }
                recycleViewAdapter.clothList = clothList
                recycleViewAdapter.notifyDataSetChanged()

                val gridLayoutManager = GridLayoutManager(applicationContext, 2)
                clothResultLayout.layoutManager = gridLayoutManager
            }
            .addOnFailureListener{ exception ->
                Log.w("tag: ", "Error getting doc", exception)
            }
    }
}