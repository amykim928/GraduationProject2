package com.example.graduationproject

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SearchResultActivity  : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val keyword = intent.getStringExtra("keyword")

        setContentView(R.layout.activity_search_result)
        val keywordResultTitle = findViewById<TextView>(R.id.keywordResultTitle)
        keywordResultTitle.text = keyword

    }
}