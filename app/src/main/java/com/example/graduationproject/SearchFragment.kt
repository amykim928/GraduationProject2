package com.example.graduationproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.databinding.ActivityMainBinding
import com.example.graduationproject.databinding.FragmentSearchBinding
import com.example.graduationproject.dataset.closetData
import com.example.graduationproject.dataset.recentData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp

private var recentList = mutableListOf<recentData>()
lateinit var recentRecyclerAdapter: RecentRecyclerAdapter
//검색페이지 Fragment
class SearchFragment : Fragment(), View.OnClickListener{

    lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity as MainActivity
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val keyword = view.findViewById<EditText>(R.id.keywordText)
        val keywordBtn = view.findViewById<ImageButton>(R.id.keywordBtn)

        //최근 사용자가 본 의상 recyclerView에 데이터 가져오기
        //해당 옷 사진 누르면 detail 화면으로 이동(img_url, brand_id, category_id, style 가지고 있음)
        val recentViewRecycle = view?.findViewById<RecyclerView>(R.id.recentViewRecycler)


        recentRecyclerAdapter = context?.let { RecentRecyclerAdapter(it) }!!
        recentViewRecycle?.adapter = recentRecyclerAdapter

        val db = Firebase.firestore
        db.collection("recentItem")//파이어베이스
            .get() //필드에 해당하는 데이터 가져오기
            .addOnSuccessListener { result -> //성공시
                recentList.clear()
                if (result.isEmpty){
                    recentViewRecycle?.setVisibility(View.GONE);
                }
                else {
                    for(doc in result) {
                        Log.d("tag: ", "${doc.id} => ${doc.data}")
                        recentList.add(
                            recentData(
                                doc.data["brand_id"] as String,
                                doc.data["category_id"] as String,
                                doc.data["img_url"] as String,
                                doc.data["style"] as String,
//                                doc.data["created_at"] as Timestamp
                            )
                        )
                    }
                    recentRecyclerAdapter.recentList = recentList
                    recentRecyclerAdapter.notifyDataSetChanged() //adapter 새로고침

                    val gridLayoutManager = GridLayoutManager(context, 1, LinearLayoutManager.HORIZONTAL, true)
                    recentViewRecycle?.layoutManager = gridLayoutManager
                }
            }
            .addOnFailureListener{ exception ->
                Log.w("tag: ", "Error getting doc", exception)
            }

        keywordBtn.setOnClickListener { //검색 버튼 activity
            val keywordStr = keyword.text.toString() //입력한 키워드
            val myIntent = Intent(activity, SearchResultActivity::class.java)
            myIntent.putExtra("keyword", keywordStr) //검색 결과 화면으로 검색한 키워드 넘김
            startActivity(myIntent) //검색 결과 화면으로 이동
        }

        val searchBtn1 = view.findViewById<Button>(R.id.searchBtn1)
        searchBtn1.setOnClickListener(this)
        val searchBtn2 = view.findViewById<Button>(R.id.searchBtn2)
        searchBtn2.setOnClickListener(this)
        val searchBtn3 = view.findViewById<Button>(R.id.searchBtn3)
        searchBtn3.setOnClickListener(this)
        val searchBtn4 = view.findViewById<Button>(R.id.searchBtn4)
        searchBtn4.setOnClickListener(this)
        val searchBtn5 = view.findViewById<Button>(R.id.searchBtn5)
        searchBtn5.setOnClickListener(this)
        val searchBtn6 = view.findViewById<Button>(R.id.searchBtn6)
        searchBtn6.setOnClickListener(this)
        val searchBtn7 = view.findViewById<Button>(R.id.searchBtn7)
        searchBtn7.setOnClickListener(this)
        val searchBtn8 = view.findViewById<Button>(R.id.searchBtn8)
        searchBtn8.setOnClickListener(this)
        val searchBtn9 = view.findViewById<Button>(R.id.searchBtn9)
        searchBtn9.setOnClickListener(this)
        val searchBtn10 = view.findViewById<Button>(R.id.searchBtn10)
        searchBtn10.setOnClickListener(this)
        val searchBtn11 = view.findViewById<Button>(R.id.searchBtn11)
        searchBtn11.setOnClickListener(this)
        val searchBtn12 = view.findViewById<Button>(R.id.searchBtn12)
        searchBtn12.setOnClickListener(this)
        val searchBtn13 = view.findViewById<Button>(R.id.searchBtn13)
        searchBtn13.setOnClickListener(this)
        val searchBtn14 = view.findViewById<Button>(R.id.searchBtn14)
        searchBtn14.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        val button = v as Button
        val myIntent = Intent(activity, SearchResultActivity::class.java)
        myIntent.putExtra("keyword", button.text) //검색 결과 화면으로 검색한 키워드 넘김
        startActivity(myIntent) //검색 결과 화면으로 이동
    }
}