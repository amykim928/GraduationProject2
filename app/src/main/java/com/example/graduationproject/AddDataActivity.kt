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
    lateinit var detector: YoloClassfier

    //특정 구역이 40%이상의 확률로 특정 카테고리로 판정할 때 쓰는 변수
    val MINIMUM_CONFIDENCE_TF_OD_API=0.4f

    //가로 세로 길이용 변수
    protected var previewWidth = 0
    protected var previewHeight = 0
    val TF_OD_API_INPUT_SIZE = 416

    private var frameToCropTransform: Matrix? = null
    private var cropToFrameTransform: Matrix? = null
    lateinit var tracker: MultiBoxTracker

    //이미지 잘라내기를 위한 변수인데,
    //이미지를 바로 잘라내지 않고 나중에 잘라내기 위해 false로 했습니다.
    private val MAINTAIN_ASPECT = false

    //회전과 관련된 변수
    private val sensorOrientation = 90

    //양자화
    //모델을 가볍게 쓰기 위해 양자화를 쓰는 경우도 있긴한데, 전 안씁니다.
    private val TF_OD_API_IS_QUANTIZED = false

    //나중에 모델 더 좋게 학습하면 모델이름을 바꾸거나 업데이트하겠죠.  "yolov4-tiny-416.tflite"
    //모델 이름과 경로, obj.txt는 이미지의 카테고리입니다(셔츠, 팬츠 같은)
    private val TF_OD_API_MODEL_FILE = "yolov4_2.tflite"

    private val TF_OD_API_LABELS_FILE = "file:///android_asset/obj.txt"
    //검출하는 부분은 여기다가 두었습니다.
    var mappping = mutableMapOf<Int, String>(0 to "탑", 1 to "블라우스", 2 to "티셔츠", 3 to "니트웨어",
        4 to "셔츠", 5 to "브라탑", 6 to "후드티", 7 to "청바지", 8 to "팬츠", 9 to "스커트",
        10 to "레깅스", 11 to "조거팬츠" , 12 to "코트",  13 to "재킷", 14 to "점퍼", 15 to "패딩",
        16 to "베스트", 17 to "가디건", 18 to "짚업", 19 to "드레스" , 20 to "점프수트", 21 to "맨투맨"
    )

    var mappping1 = mutableMapOf<Int, String>(0 to "트래디셔널", 1 to "매니시", 2 to "페미닌", 3 to "에스닉",
        4 to "컨템포러리", 5 to "내추럴", 6 to "젠더플루이드", 7 to "스포티", 8 to "서브컬쳐", 9 to "캐주얼", 10 to "로맨틱", 11 to "클래식"
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

        var closetImage = findViewById<ImageView>(R.id.recommendImage)
        init()
        initbox()

        if(intent.hasExtra("img_url")){
            Glide.with(this).load(cimg_url).into(closetImage)
//            Log.d("####################cstyle: ", cstyle)
//            Log.d("####################ccategory_id: ", ccategory_id)
            val res_style = mappping1.filterValues { it == cstyle }.keys.toString()
            val res_style_toInt = res_style.replace("[^0-9]".toRegex(), "").toInt()
//            Log.d("#################res_style: ", "$res_style_toInt")
            binding.styleSpinner.setSelection(res_style_toInt)
            val res_category = mappping.filterValues { it == ccategory_id }.keys.toString()
            val res_category_toInt = res_category.replace("[^0-9]".toRegex(), "").toInt()
//            Log.d("#################res_category: ", "${res_category_toInt}")
            binding.categorySpinner.setSelection(res_category_toInt)

        } else{
            binding.recommendImage.setImageBitmap(bitmap)
        }



        binding.recommendButton.setOnClickListener {
            Log.i("tag 3:","recommendBtn")
            val bits=binding.recommendImage.drawable.toBitmap()
            var cloth_type=""
            val underlist= listOf<Int>(7,8,9,10,11)
            Log.i("tag 3:",binding.categorySpinner.id.toString())
            if (binding.categorySpinner.id in underlist ){
                cloth_type="하의"
            }else{
                cloth_type="상의"
            }

            val styleInt=binding.styleSpinner.selectedItemId.toInt()
            val hashMap= hashMapOf(Pair(bitmapToString(bits),ImageFeatures(cloth_type,0)))
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

    private fun initbox() {
        previewHeight = 416
        previewWidth = 416

        //matrix 416x416 행렬이 생겼다 정도로 이해하면 될듯합니다.
        frameToCropTransform = ImageUtils.getTransformationMatrix(
            previewWidth, previewHeight,
            TF_OD_API_INPUT_SIZE, TF_OD_API_INPUT_SIZE,
            sensorOrientation, MAINTAIN_ASPECT
        )

        cropToFrameTransform = Matrix()
        //역행렬 찾음 cropToFrameTramsForm에.
        frameToCropTransform!!.invert(cropToFrameTransform)


        //activity Detect에, trackingOverlay가 있는데,
        //handleResult에 canvas.drawRect(location, paint)의 주석을 지우면 아래가 작동하여, 네모를 그립니다.
        tracker = MultiBoxTracker(this)

        //기본 설정입니다.
        tracker.setFrameConfiguration(
            TF_OD_API_INPUT_SIZE,
            TF_OD_API_INPUT_SIZE,
            sensorOrientation
        )
        try {
            Log.i("main fail :","check")
            //YoloDetector를 만드는 부분입니다. 416x416 사이즈에, 양자화를 사용하지 않는다- 라는 걸 넘겨줍니다.
            //asset은 학습한 모델의 경로를 알기 위해 넘겨줍니다.
            detector = YoloClassfier().create(
                assets,
                TF_OD_API_MODEL_FILE,
                TF_OD_API_LABELS_FILE,
                TF_OD_API_IS_QUANTIZED
            )

        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("main fail :","Exception initializing classifier!")
            Toast.makeText(
                applicationContext, "Classifier could not be initialized", Toast.LENGTH_SHORT
            ).show()
            finish()
        }
        Toast.makeText(this,"의상 검출",Toast.LENGTH_SHORT).show()
        //비동기적으로
        //의상 탐지를 위해 detector에 bitmap이미지를 넣습니다.
        val results: List<YoloInterfaceClassfier.Recognition>? = detector.recognizeImage(bitmap)
        if (results!=null){
            resultList=results
        }else{
            Toast.makeText(this,"의상검출 실패",Toast.LENGTH_SHORT).show()
        }
        var max_idx=0
        var max_confidence:Float=0.0F
        for ((idx,result) in resultList.withIndex()){
            if(result.confidence!! >max_confidence){
                max_idx=idx
                max_confidence=result.confidence
            }
        }
//
        if(resultList.isNotEmpty()){
            mappping[resultList[max_idx].detectedClass]?.let { Log.i("check resultlist", it) }
            binding.categorySpinner.setSelection(resultList[max_idx].detectedClass)
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
