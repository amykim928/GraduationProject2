package com.example.graduationproject

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.databinding.ActivityAddDataBinding
import com.example.graduationproject.databinding.ActivityRecommendBinding
import com.example.graduationproject.dataset.API
import com.example.graduationproject.dataset.ImageFeatures
import retrofit2.Call
import retrofit2.Retrofit
import java.io.File

class RecommendActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRecommendBinding
    private lateinit var recommendBitmap: Bitmap
    private lateinit var styleBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getIntent=intent.getStringExtra("simliarity")
        binding.editTextT1.setText(getIntent)

        val cacheFile = File(cacheDir, "savedRecommend.jpg").path
        recommendBitmap= BitmapFactory.decodeFile(cacheFile)

        val cacheFile2=File(cacheDir, "style.jpg").path
        styleBitmap= BitmapFactory.decodeFile(cacheFile2)

        binding.recommendImage.setImageBitmap(recommendBitmap)
        binding.styleImage.setImageBitmap(styleBitmap)

    }
}