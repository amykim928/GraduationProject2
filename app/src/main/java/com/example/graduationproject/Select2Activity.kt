package com.example.graduationproject

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.graduationproject.databinding.ActivitySelect2Binding
import com.example.graduationproject.dataset.combinationData
import com.example.graduationproject.dataset.recentData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Select2Activity: AppCompatActivity() {
    lateinit var binding:ActivitySelect2Binding
    val combinationList= mutableListOf<combinationData>()
    var combinationNumber=0
    var topWear = mutableListOf<String>("탑", "블라우스","티셔츠","니트웨어",
        "셔츠",  "브라탑", "후드티", "코트", "재킷",  "점퍼", "패딩",
        "베스트", "가디건", "짚업", "드레스" , "점프수트", "맨투맨"
    )
    var underWear = mutableListOf<String>("청바지",  "팬츠", "스커트", "레깅스", "조거팬츠")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySelect2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        combinationNumber=0
        binding.leftButton.setOnClickListener {
            Log.i("check Number",combinationNumber.toString())
            combinationNumber-=1
            if(combinationNumber==-1){
                combinationNumber=combinationList.size-1
            }
            setImages()
        }
        binding.rightButton.setOnClickListener {
            Log.i("check Number",combinationNumber.toString())
            combinationNumber+=1
            if(combinationNumber==combinationList.size){
                combinationNumber=0
            }
            setImages()
        }
        val db = Firebase.firestore
        db.collection("combinationData")//파이어베이스
            .orderBy("cl_intro").limit(10)
            .get() //필드에 해당하는 데이터 가져오기
            .addOnSuccessListener { result -> //성공시
                if (result.isEmpty){
                    Log.w("tag: ", "result empty")
                }
                else {
                    for(doc in result) {
                        combinationList.add(
                            combinationData(
                                doc.data["cl_intro"] as String,
                                doc.data["category_id_1"] as String,
                                doc.data["category_id_2"] as String,
                                doc.data["img_base64_1"] as String,
                                doc.data["img_base64_2"] as String,
                                doc.data["img_url_1"] as String,
                                doc.data["img_url_2"] as String,
                                doc.data["style_1"] as String,
                                doc.data["style_2"] as String
                            )
                        )
                    }
                }
//                Log.i("check list",combinationList[0].toString())
                setImages()
            }
            .addOnFailureListener{ exception ->
                Log.w("tag: ", "Error getting doc", exception)
            }


    }

    private fun setImages() {
        if(combinationList[combinationNumber].category_id_1 in topWear){
            val img_url1=combinationList[combinationNumber].img_url_1
            val img_url2=combinationList[combinationNumber].img_url_2
            if(img_url1!="null"){
                Glide.with(this).load(img_url1).into(binding.topView)
            }else{
                binding.topView.setImageBitmap(stringToBitmap(combinationList[combinationNumber].img_base64_1))
            }
            if(img_url2!="null"){
                Glide.with(this).load(img_url2).into(binding.underView)
            }else{
                binding.underView.setImageBitmap(stringToBitmap(combinationList[combinationNumber].img_base64_2))
            }
        }else{
            val img_url1=combinationList[combinationNumber].img_url_1
            val img_url2=combinationList[combinationNumber].img_url_2
            if(img_url1!="null"){
                Glide.with(this).load(img_url1).into(binding.underView)
            }else{
                binding.underView.setImageBitmap(stringToBitmap(combinationList[combinationNumber].img_base64_1))
            }
            if(img_url2!="null"){
                Glide.with(this).load(img_url2).into(binding.topView)
            }else{
                binding.topView.setImageBitmap(stringToBitmap(combinationList[0].img_base64_2))
            }
        }

    }


    private fun stringToBitmap(encodedString: String): Bitmap {

        val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}