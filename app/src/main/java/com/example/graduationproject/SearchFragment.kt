package com.example.graduationproject

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


class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity as MainActivity
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val keyword = view.findViewById<EditText>(R.id.keywordText)
        val keywordBtn = view.findViewById<ImageButton>(R.id.keywordBtn)
        keywordBtn.setOnClickListener {
            val keywordStr = keyword.text.toString()
            val myIntent = Intent(activity, SearchResultActivity::class.java)
            myIntent.putExtra("keyword", keywordStr)
            startActivity(myIntent)
        }
        return view
    }
}