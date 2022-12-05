package com.example.graduationproject

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.graduationproject.dataset.API
import com.example.graduationproject.dataset.onlyFeatureVector
import com.example.graduationproject.utils.LoadingDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream


class ResultDetailActivity: AppCompatActivity() {
    lateinit var mCallImgVector: Call<onlyFeatureVector>
    lateinit var imageVector:List<Float>
    lateinit var mRetrofit: Retrofit // 사용할 레트로핏 객체입니다.
    lateinit var mRetrofitAPI: API.RetrofitAPI // 레트로핏 api객체입니다.
    lateinit var bitmap: Bitmap
    lateinit var dialog: LoadingDialog
    private var brand_name = listOf<String>(
        "","블랙야크", "유니클로", "abc마트", "", "X", "", "와릿이즌", "예일", "클로티"
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_detail)

        val toSearchIntent =Intent(this, MainActivity::class.java)
        toSearchIntent.putExtra("fragment_id",1)
        toSearchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        toSearchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        //recentItem 컬렉션에 넣을 데이터를 hashmap에 담아 recyclerview에 넣어줌
        val data = hashMapOf(
            "img_url" to intent.getStringExtra("img_url").toString(),
            "img_base64 " to "",
            "category_id" to intent.getStringExtra("category_id").toString(),
            "brand_id" to intent.getStringExtra("brand_id").toString(),
            "style" to intent.getStringExtra("style").toString()
        )

//        data.put("created_at", LocalDateTime.now().toString())
        val a = (10000000000000 - System.currentTimeMillis())
        data.put("created_at", a.toString())
        Log.d("################System.currentTimeMillis(): ", "${a.toLong()}")

        val db = Firebase.firestore
        db.collection("recentItem").add(data)
            .addOnSuccessListener{
                //Toast.makeText(this, "최근 본 의상에 저장", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // 실패할 경우
                Log.w("ResultDetailActivity", "Error getting documents: $exception")
            }


        var img_url = intent.getStringExtra("img_url").toString()
        var category_id = intent.getStringExtra("category_id").toString()
        var brand_id = intent.getStringExtra("brand_id").toString()
        var style = intent.getStringExtra("style").toString()
        //var cl_intro = intent.getStringExtra("cl_intro").toString()


        val resultImage = findViewById<ImageView>(R.id.resultImage)
        val resultCategory = findViewById<TextView>(R.id.resultCategory)
        val resultBrand = findViewById<TextView>(R.id.resultBrand)
        val resultStyle = findViewById<TextView>(R.id.resultStyle)
        val saveBtn = findViewById<Button>(R.id.saveBtn)

        resultCategory.text = "카테고리: $category_id"
        resultBrand.text = "브랜드: " + brand_id
        resultStyle.text = "스타일: " + style
        Glide.with(this).load(img_url).into(resultImage)

        Glide.with(this)
            .asBitmap()
            .load(img_url)
            .into(object : CustomTarget<Bitmap>(){
                override fun onLoadCleared(placeholder: Drawable?) {
                    Log.i("glide bitmap","not ready")
                }
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    Log.i("glide bitmap","ready")
                    setRetrofit(resource)
                    Log.i("glide bitmap",resource.width.toString())
                }
            })

        saveBtn.setOnClickListener { //저장하기 버튼 클릭 시
            val timeLong=System.currentTimeMillis()
            val timeId= "app$timeLong"

            val data = hashMapOf(
                "cl_intro" to timeId,
                "img_url" to intent.getStringExtra("img_url").toString(),
                "category_id" to intent.getStringExtra("category_id").toString(),
                "style" to intent.getStringExtra("style").toString(),
                "img_base64 " to "",
                "imageVector" to imageVector

            )
            val db = Firebase.firestore
            db.collection("closetData").add(data)
                .addOnSuccessListener{
                    Toast.makeText(this, "저장했습니다!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    // 실패할 경우
                    Log.w("ResultDetailActivity", "Error getting documents: $exception")
                }
            startActivity(toSearchIntent)
            finish()
        }
    }
    private fun setRetrofit(bitmap: Bitmap) {
        //레트로핏으로 가져올 url설정하고 세팅
        val gson : Gson = GsonBuilder()
            .setLenient()
            .create()
        mRetrofit = Retrofit
            .Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        //http://10.0.2.2:5000  //에뮬레이터 구동
        // http://192.168.115.236:5000</string>    핫스팟시,
        //인터페이스로 만든 레트로핏 api요청 받는 것 변수로 등록
        mRetrofitAPI = mRetrofit.create(API.RetrofitAPI::class.java)

        Log.i("tag retrofit init :","start")

        mCallImgVector=mRetrofitAPI.postGetFeature(bitmapToString(bitmap))
        mCallImgVector.enqueue(mRetrofitCallback3)
    }

    private val mRetrofitCallback3 = (object : retrofit2.Callback<onlyFeatureVector> {
        override fun onResponse(
            call: Call<onlyFeatureVector>,
            response: Response<onlyFeatureVector>
        ) {
            Log.i("tag retrofit vector",response.body()?.recommend_feature.toString())
            imageVector= response.body()?.recommend_feature!!
        }

        override fun onFailure(call: Call<onlyFeatureVector>, t: Throwable) {
            Log.i("tag retrofit :", t.message.toString())
        }

    })


    private fun bitmapToString(bitmap: Bitmap): String {

        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

}