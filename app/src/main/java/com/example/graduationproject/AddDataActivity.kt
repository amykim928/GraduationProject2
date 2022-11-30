package com.example.graduationproject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Selection.setSelection
import android.util.Base64
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.example.graduationproject.classfier.YoloClassfier
import com.example.graduationproject.classfier.YoloInterfaceClassfier
import com.example.graduationproject.databinding.ActivityAddDataBinding
import com.example.graduationproject.dataset.API
import com.example.graduationproject.dataset.ImageFeatures
import com.example.graduationproject.dataset.ImgDataModel
import com.example.graduationproject.dataset.getImages

import com.example.graduationproject.env.ImageUtils
import com.example.graduationproject.tracker.MultiBoxTracker
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import kotlin.reflect.typeOf

class AddDataActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddDataBinding
    private lateinit var bitmap: Bitmap

    var text=""

    //retrofit
    lateinit var mRetrofit: Retrofit // 사용할 레트로핏 객체입니다.
    lateinit var mRetrofitAPI: API.RetrofitAPI // 레트로핏 api객체입니다.
    lateinit var mCallImg:Call<String>
    lateinit var getImages:Call<getImages>

    lateinit var resultList : List<YoloInterfaceClassfier.Recognition>
    //의상 검출을 위한 변수

    //검출하는 부분은 여기다가 두었습니다.
    var mappping = mutableMapOf<Int, String>(0 to "탑", 1 to "블라우스", 2 to "티셔츠", 3 to "니트웨어",
        4 to "셔츠", 5 to "브라탑", 6 to "후드티", 7 to "청바지", 8 to "팬츠", 9 to "스커트",
        10 to "레깅스", 11 to "조거팬츠" , 12 to "코트",  13 to "재킷", 14 to "점퍼", 15 to "패딩",
        16 to "베스트", 17 to "가디건", 18 to "짚업", 19 to "드레스" , 20 to "점프수트", 21 to "맨투맨"
    )

    var mappping1 = mutableMapOf<Int, String>(0 to "트래디셔널", 1 to "매니시", 2 to "페미닌", 3 to "에스닉",
        4 to "컨템포러리", 5 to "내추럴", 6 to "젠더플루이드", 7 to "스포티", 8 to "서브컬쳐", 9 to "캐주얼"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddDataBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val cacheFile = File(cacheDir, "cropped.jpg").path
        bitmap=BitmapFactory.decodeFile(cacheFile)
        var cimg_url = intent.getStringExtra("img_url").toString()
        var ccategory_id = intent.getStringExtra("category_id").toString()
        var cstyle = intent.getStringExtra("style").toString()
        var img_base = intent.getStringExtra("img_base").toString()

        if(img_base=="exist"){
            bitmap = BitmapFactory.decodeFile( File(cacheDir, "cropped.jpg").path)
        }


        if(cstyle=="클래식"){
            cstyle="트래디셔널"
        }
        var closetImage = findViewById<ImageView>(R.id.recommendImage)
        init()

        if(img_base=="exist"){
            binding.recommendImage.setImageBitmap(bitmap)

        } else{
            Glide.with(this).load(cimg_url).into(closetImage)
            val res_style = mappping1.filterValues { it == cstyle }.keys.toString()
            val res_style_toInt = res_style.replace("[^0-9]".toRegex(), "").toInt()
            binding.styleSpinner.setSelection(res_style_toInt)
            val res_category = mappping.filterValues { it == ccategory_id }.keys.toString()
            val res_category_toInt = res_category.replace("[^0-9]".toRegex(), "").toInt()
            binding.categorySpinner.setSelection(res_category_toInt)

        }


        binding.recommendButton.setOnClickListener {
            Log.i("tag 3:","recommendBtn")
            val bits=binding.recommendImage.drawable.toBitmap()
            var cloth_type=""
            val underlist= listOf<Int>(7,8,9,10,11)
            Log.i("tag 3:",binding.categorySpinner.selectedItemId.toString())
            if (binding.categorySpinner.selectedItemId.toInt() in underlist ){
                cloth_type="하의"
            }else{
                cloth_type="상의"
            }

            val styleInt=binding.styleSpinner.selectedItemId.toInt()
            val hashMap= hashMapOf(Pair(bitmapToString(bits),ImageFeatures(cloth_type,styleInt)))
            getImages=mRetrofitAPI.postPredict(hashMap)
            getImages.enqueue(mRetrofitCallback2)
        }

        setRetrofit()
    }
    private fun setRetrofit() {
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
        mCallImg=mRetrofitAPI.postImgStyle(bitmapToString(bitmap))
        mCallImg.enqueue(mRetrofitCallback)
    }

    fun init(){
        val style_items = resources.getStringArray(R.array.style_array)
        val category_items = resources.getStringArray(R.array.category_array)

        val myadapter_style = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,style_items)
        binding.styleSpinner.adapter=myadapter_style
//        Log.d("##########################", "$style_items")
//        Log.d("##########################myadapter_style", "$myadapter_style")

        val myadapter_category=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,category_items)
        binding.categorySpinner.adapter=myadapter_category
        binding.backButton.setOnClickListener {
            finish()
        }

    }

    private val mRetrofitCallback2 =(object  :retrofit2.Callback<getImages>{
        override fun onResponse(call: Call<getImages>, response: Response<getImages>) {
            val result=response.body()
            val recommendString= result?.recommend_img
            val simliarity= result?.simliarity?.toFloat()
            val styleString= result?.style_img

            val recommendImg0= recommendString?.let { stringToBitmap(it) }
            val styImg=styleString?.let{stringToBitmap(it)}
            text=simliarity.toString()

            val storage=cacheDir
            val RecommendName= "savedRecommend.jpg"
            val StyleName = "style.jpg"
            val tempFile = File(storage, RecommendName)
            tempFile.createNewFile()
            if (recommendImg0 != null) {
                recommendImg0.compress(Bitmap.CompressFormat.JPEG,100, FileOutputStream(tempFile))
            }
            val tempFile2=File(storage,StyleName)
            if (styImg != null) {
                styImg.compress(Bitmap.CompressFormat.JPEG,100, FileOutputStream(tempFile2))
            }
            Log.i("tag retrofit2 :",simliarity.toString())
            val intent= Intent(this@AddDataActivity,RecommendActivity::class.java)
            intent.putExtra("simliarity",simliarity.toString())
            startActivity(intent)
        }

        override fun onFailure(call: Call<getImages>, t: Throwable) {
            Log.i("tag retrofit2 :",t.message.toString())
        }


    })
    private val mRetrofitCallback = (object : retrofit2.Callback<String> {
    override fun onResponse(call: Call<String>, response: Response<String>) {
        val result = response.body()
        if (result != null) {
            Log.i("tag retrofit :",result)
            binding.styleSpinner.setSelection(result.toInt())
        }
    }

    override fun onFailure(call: Call<String>, t: Throwable) {
        Log.i("tag retrofit :",t.message.toString())
    }


})//Json객체를 응답받는 콜백 객체


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
