package com.example.graduationproject

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.adapters.RecycleViewAdapter
import com.example.graduationproject.dataset.clothData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private var clothList = mutableListOf<clothData>()
lateinit var recycleViewAdapter: RecycleViewAdapter

private var category_name =  listOf<String>(
    "탑", "블라우스", "티셔츠", "니트웨어", "셔츠", "브라탑", "후드티",
    "청바지", "팬츠", "스커트", "레깅스", "조거팬츠", "코트", "재킷",
    "점퍼", "패딩", "베스트", "가디건", "짚업", "점프수트", "드레스")

private var brand_name = listOf<String>(
    "", "로라로라","유니클로", "스파오", "아뜨랑스", "x", "리", "와릿이즌", "예일", "클로티"
)

class SearchResultActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        var keyword = intent.getStringExtra("keyword").toString() //검색 화면에서 입력한 키워드 받기

        val keywordResultTitle = findViewById<TextView>(R.id.keywordResultTitle)
        keywordResultTitle.text = keyword + "에 대한 결과입니다."

        val keywordText = findViewById<TextView>(R.id.keywordText)
        keywordText.text = keyword
        val keywordBtn = findViewById<ImageButton>(R.id.keywordBtn)
        keywordBtn.setOnClickListener {
            val keywordStr = keywordText.text.toString()
            keyword = keywordStr
            finish()
            val intent = getIntent()
            intent.putExtra("keyword", keyword)
            startActivity(intent)
        }

        var keywordField = "category_id"
        if(brand_name.contains(keyword)){
            keywordField = "brand_id"
        } //사용자가 브랜드를 찾는 경우 필드를 brand_id로 변경

        val clothResultView = findViewById<RecyclerView>(R.id.clothResultView)
        clothResultView.addItemDecoration(VerticalItemDecorator(20))
        clothResultView.addItemDecoration(HorizontalItemDecorator(20))
        recycleViewAdapter = RecycleViewAdapter(this)
        clothResultView.adapter = recycleViewAdapter //검색 결과 목록용 adapter

        val emptyResultView = findViewById<TextView>(R.id.emptyResultView)

        val db = Firebase.firestore
        db.collection("clothData") //파이어베이스
            .whereEqualTo(keywordField, keyword)
            .get() //필드에 해당하는 데이터 가져오기
            .addOnSuccessListener { result -> //성공시
                clothList.clear()
                if (result.isEmpty){
                    clothResultView.setVisibility(View.GONE);
                    emptyResultView.setVisibility(View.VISIBLE);
                } else {
                    for(doc in result) {
                        Log.d("tag: ", "${doc.id} => ${doc.data}")
                        clothList.add(
                            clothData(doc.data.get("brand_id") as String,
                                doc.data.get("category_id") as String,
                                doc.data.get("cl_intro") as String,
                                doc.data.get("cl_pd_num") as String,
                                doc.data.get("color") as String,
                                doc.data.get("id") as Long,
                                doc.data.get("img_url") as String,
                                doc.data.get("style") as String) //이후 검색 결과 화면에서 옷을 선택한 후에 나오는 화면을 위해 데이터 가공하여 추가
                        )
                    }
                    recycleViewAdapter.clothList = clothList
                    recycleViewAdapter.notifyDataSetChanged() //adapter 새로고침

                    val gridLayoutManager = GridLayoutManager(applicationContext, 2) //2칸으로 나오게
                    clothResultView.layoutManager = gridLayoutManager
                    emptyResultView.setVisibility(View.GONE);
//                    dialog.dismiss()
                }
            }
            .addOnFailureListener{ exception ->
                Log.w("tag: ", "Error getting doc", exception)
            }


    }
}