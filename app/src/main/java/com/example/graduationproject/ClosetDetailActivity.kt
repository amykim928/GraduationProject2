package com.example.graduationproject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.example.graduationproject.dataset.closetData
import com.example.graduationproject.dbhelper.DbHelper.Companion.id
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class ClosetDetailActivity: AppCompatActivity() {
    lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closet_detail)

        var cimg_url = intent.getStringExtra("img_url").toString()
        var ccategory_id = intent.getStringExtra("category_id").toString()
        var cstyle = intent.getStringExtra("style").toString()
        val cimg_base=intent.getStringExtra("img_base").toString()



        val closetImage = findViewById<ImageView>(R.id.closetImage)
        val goRecommendBtn = findViewById<Button>(R.id.goRecommendBtn)
        val deleteBtn = findViewById<ImageButton>(R.id.deleteBtn)
        Log.d("####################tag: ", "$ccategory_id")
        if(cimg_base=="exist"){
            val cacheFile = File(cacheDir, "cropped.jpg").path
            bitmap = BitmapFactory.decodeFile(cacheFile)
            closetImage.setImageBitmap(bitmap)
        }else{

            Glide.with(this).load(cimg_url).into(closetImage)
        }

//        Log.d("####################IDIDIDIDID: ", "${Firebase.firestore.document(cimg_url).id}")


        goRecommendBtn.setOnClickListener{
            Toast.makeText(applicationContext,"조합 보기", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, AddDataActivity::class.java)
            intent.putExtra("img_url", cimg_url)
            intent.putExtra("category_id", ccategory_id)
            intent.putExtra("style", cstyle)
            intent.putExtra("img_base",cimg_base)
            startActivity(intent)
        }

//        val db = Firebase.firestore
//        deleteBtn.setOnClickListener{
//            Toast.makeText(applicationContext,"삭제 되었습니다.", Toast.LENGTH_SHORT).show()
//            db.collection("closetData").document().delete()
//        }
    }


}