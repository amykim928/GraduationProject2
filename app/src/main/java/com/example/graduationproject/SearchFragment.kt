package com.example.graduationproject

import SearchResultActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment

//검색페이지 Fragment
class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity as MainActivity
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val keyword = view.findViewById<EditText>(R.id.keywordText)
        val keywordBtn = view.findViewById<ImageButton>(R.id.keywordBtn)

        keywordBtn.setOnClickListener { //검색 버튼 activity
            val keywordStr = keyword.text.toString() //입력한 키워드
            val myIntent = Intent(activity, SearchResultActivity::class.java)
            myIntent.putExtra("keyword", keywordStr) //검색 결과 화면으로 검색한 키워드 넘김
            startActivity(myIntent) //검색 결과 화면으로 이동
        }
        return view
    }
}