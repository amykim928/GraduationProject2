package com.example.graduationproject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityRecommendWithFirebaseBinding
import com.example.graduationproject.dataset.closetData
import com.example.graduationproject.dataset.clothData
import com.example.graduationproject.dataset.recentData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.io.File

class RecommendWithFirebase : AppCompatActivity() {
    lateinit var binding :ActivityRecommendWithFirebaseBinding
    var checkTop=1
    var closetLists = mutableListOf<closetData>()
    var topWear = mutableListOf<String>("탑", "블라우스","티셔츠","니트웨어",
        "셔츠",  "브라탑", "후드티", "코트", "재킷",  "점퍼", "패딩",
        "베스트", "가디건", "짚업", "드레스" , "점프수트", "맨투맨"
    )
    var underWear = mutableListOf<String>("청바지",  "팬츠", "스커트", "레깅스", "조거팬츠")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRecommendWithFirebaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cl_intro= intent.getStringExtra("doc")
        val category_cloth=intent.getStringExtra("category_id")
        if(category_cloth in topWear ){
            checkTop=1
        }else{
            checkTop=0
        }
        Log.i("get cl_intro1",cl_intro.toString())
        val recommendimg=binding.UpClothImage
        val cacheFile2= File(cacheDir, "saveBase.jpg").path
        val baseBitmap= BitmapFactory.decodeFile(cacheFile2)
        if(checkTop==0){
            binding.underClothImage.setImageBitmap(baseBitmap)
            binding.underClothText.setText("참고 의상")
        }else{
            binding.UpClothImage.setImageBitmap(baseBitmap)
            binding.UpClothText.setText("참고 의상")
        }



        val db = Firebase.firestore
        db.collection("closetData")//파이어베이스
            .whereEqualTo("cl_intro",cl_intro)
            .get() //필드에 해당하는 데이터 가져오기
            .addOnSuccessListener { result -> //성공시
                Log.d("tag: ", "성공")
                closetLists.clear()
                for(doc in result) {
                        Log.d("tag: ", "${doc.id} => ${doc.data}")
                    val img64= doc.data.get("img_base64 ") as String
                    val img_url=doc.data.get("img_url") as String
                    closetLists.add(
                        closetData(
                            doc.data.get("category_id") as String,
                            doc.data.get("img_base64 ") as String,
                            doc.data.get("cl_intro") as String,
                            doc.data.get("img_url") as String,
                            doc.data.get("style") as String,
                        ))
                    if(img_url=="null"){
                        val recommendBitmap=stringToBitmap(img64)
                        if(checkTop==0){
                            binding.UpClothImage.setImageBitmap(recommendBitmap)
                            binding.UpClothText.setText("추천 의상")
                        }else{
                            binding.underClothImage.setImageBitmap(recommendBitmap)
                            binding.underClothText.setText("추천 의상")
                        }
                        break
                    }else{
                        if(checkTop==0){
                            Glide.with(this).load(img_url).into(binding.UpClothImage)
                            binding.UpClothText.setText("추천 의상")
                        }else{
                            Glide.with(this).load(img_url).into(binding.underClothImage)
                            binding.underClothText.setText("추천 의상")
                        }

                        break
                    }
                //이후 검색 결과 화면에서 옷을 선택한 후에 나오는 화면을 위해 데이터 가공하여 추가)
                }
            }
            .addOnFailureListener{ exception ->
                Log.w("tag: ", "Error getting doc", exception)
            }

        binding.backToClosetButton.setOnClickListener{
            finish()
        }
        binding.saveCombinationButton.setOnClickListener {
//            Log.i("closetList check",closetLists.toString())

            val img_url=closetLists[0].img_url
            val category_id=closetLists[0].category_id
            val img_base64=closetLists[0].img_base64
            val style=closetLists[0].style


            var cimg_url = intent.getStringExtra("img_url").toString()
            var ccategory_id = intent.getStringExtra("category_id").toString()
            var cstyle = intent.getStringExtra("style").toString()
            var img_base = bitmapToString(baseBitmap)
            val timeLong=System.currentTimeMillis()
            val timeId= "app$timeLong"

            val data = hashMapOf(
                "cl_intro" to timeId,
                "img_url_1" to cimg_url,
                "category_id_1" to ccategory_id,
                "style_1" to cstyle,
                "img_base64_1" to img_base,
                "img_url_2" to img_url,
                "category_id_2" to category_id,
                "style_2" to style,
                "img_base64_2" to img_base64,
            )
            val db1 = Firebase.firestore
            db1.collection("combinationData").add(data)
                .addOnSuccessListener{
                    Toast.makeText(this, "저장했습니다!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    // 실패할 경우
                    Log.w("ResultDetailActivity", "Error getting documents: $exception")
                }
            val toSearchIntent =Intent(this,MainActivity::class.java)
            toSearchIntent.putExtra("fragment_id",3)
            toSearchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
            toSearchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(toSearchIntent)
            finish()

        }



    }

    private fun stringToBitmap(encodedString: String): Bitmap {

        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }


    private fun bitmapToString(bitmap: Bitmap): String {

        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


}


/*

            val db = Firebase.firestore
            db.collection("closetData")//파이어베이스
                .whereEqualTo("cl_intro",result)
                .get() //필드에 해당하는 데이터 가져오기
                .addOnSuccessListener { result -> //성공시
                    Log.d("tag: ", "성공")
                    for(doc in result) {
                        Log.d("tag: ", "${doc.id} => ${doc.data}")
                             results=closetData(
                                doc.data.get("category_id") as String,
                                doc.data.get("img_base64 ") as String,
                                doc.data.get("cl_intro") as String,
                                doc.data.get("img_url") as String,
                                doc.data.get("style") as String,
                                )
                        Log.i("check results",results.cl_intro)
                    }

                }
                .addOnFailureListener{ exception ->
                    Log.w("tag: ", "Error getting doc", exception)
                }



 */

