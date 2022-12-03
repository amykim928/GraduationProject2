package com.example.graduationproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.ActivitySelect3Binding

class Select3Activity: AppCompatActivity() {
    lateinit var select3Binding: ActivitySelect3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        select3Binding=ActivitySelect3Binding.inflate(layoutInflater)
        setContentView(select3Binding.root)
    }
}