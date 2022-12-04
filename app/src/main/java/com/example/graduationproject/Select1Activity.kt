package com.example.graduationproject


import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.dataset.WeatherModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

// 메인 액티비티
class Select1Activity : AppCompatActivity() {
    lateinit var weatherRecyclerView : RecyclerView
    lateinit var curWeather : ImageView
    lateinit var curTemp : TextView
    lateinit var curSky : TextView
    lateinit var curHuminity : TextView
    lateinit var selectClothDetail: TextView

    private var base_date = "20221202"  // 발표 일자
    private var base_time = "1400"      // 발표 시각
    //private var nx = "55"               // 예보지점 X 좌표
    //private var ny = "127"              // 예보지점 Y 좌표
    private var curPoint: Point? = null

    @SuppressLint("SetTextI18n", "MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select1)


        // Get permission
        val permissionList = arrayOf<String>(
            // 위치 권한
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )

        // 권한 요청
        ActivityCompat.requestPermissions(this@Select1Activity, permissionList, 1)

        val curDate = findViewById<TextView>(R.id.curDate) // 오늘 날짜 텍스트뷰
        curWeather = findViewById<ImageView>(R.id.curWeather) //날씨 이미지
        curTemp = findViewById<TextView>(R.id.curTemp) //기온
        curSky = findViewById<TextView>(R.id.curSky) //하늘 상태
        curHuminity = findViewById<TextView>(R.id.curHumidity) //습도
        selectClothDetail = findViewById<TextView>(R.id.selectClothDetail) //추천할 옷

        weatherRecyclerView = findViewById<RecyclerView>(R.id.weatherRecyclerView)  // 날씨 리사이클러 뷰
        val btnRefresh = findViewById<ImageButton>(R.id.btnRefresh) // 새로고침 버튼

        // 오늘 날짜 텍스트뷰 설정
        curDate.text = SimpleDateFormat("MM월 dd일", Locale.getDefault()).format(Calendar.getInstance().time) + " 현재 날씨"

        // nx, ny지점의 날씨 가져와서 설정하기
        requestLocation()

        // <새로고침> 버튼 누를 때 날씨 정보 다시 가져오기
        btnRefresh.setOnClickListener {
            requestLocation()
        }
    }

    // 날씨 가져와서 설정하기
    private fun setWeather(nx : Int, ny : Int) {
        // 준비 단계 : base_date(발표 일자), base_time(발표 시각)
        // 현재 날짜, 시간 정보 가져오기
        val cal = Calendar.getInstance()
        base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time) // 현재 날짜
        val timeH = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 시각
        val timeM = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 분
        // API 가져오기 적당하게 변환
        base_time = WeatherDetail().getBaseTime(timeH, timeM)
        // 현재 시각이 00시이고 45분 이하여서 baseTime이 2330이면 어제 정보 받아오기
        if (timeH == "00" && base_time == "2330") {
            cal.add(Calendar.DATE, -1).toString()
            base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        }
        Log.i("response nx ny", "$nx / $ny")
        // 날씨 정보 가져오기
        // (한 페이지 결과 수 = 60, 페이지 번호 = 1, 응답 자료 형식-"JSON", 발표 날싸, 발표 시각, 예보지점 좌표)
        val call = WeatherObject.retrofitService.GetWeather(60, 1, "JSON", base_date, base_time, nx, ny)

        // 비동기적으로 실행하기
        call.enqueue(object : retrofit2.Callback<WEATHER> {
            // 응답 성공 시
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful) {
                    // 날씨 정보 가져오기
                    Log.i("response weather",response.toString())
                    val it: List<ITEM> = response.body()!!.response.body.items.item

                    // 현재 시각부터 1시간 뒤의 날씨 6개를 담을 배열
                    val weatherArr = arrayOf(WeatherModel(), WeatherModel(), WeatherModel(), WeatherModel(), WeatherModel(), WeatherModel())

                    // 배열 채우기
                    var index = 0
                    val totalCount = response.body()!!.response.body.totalCount - 1
                    for (i in 0..totalCount) {
                        index %= 6
                        when(it[i].category) {
                            "PTY" -> weatherArr[index].rainType = it[i].fcstValue     // 강수 형태
                            "REH" -> weatherArr[index].humidity = it[i].fcstValue     // 습도
                            "SKY" -> weatherArr[index].sky = it[i].fcstValue          // 하늘 상태
                            "T1H" -> weatherArr[index].temp = it[i].fcstValue         // 기온
                            else -> continue
                        }
                        index++
                    }

                    weatherArr[0].fcstTime = "현재"
                    // 각 날짜 배열 시간 설정
                    for (i in 0..5) weatherArr[i].fcstTime = it[i].fcstTime

                    curWeather.setImageResource(WeatherDetail().getRainImage(weatherArr[0].fcstTime, weatherArr[0].rainType, weatherArr[0].sky))
                    curTemp.text = weatherArr[0].temp + "°"
                    curSky.text = getSky(weatherArr[0].sky)
                    curHuminity.text = weatherArr[0].humidity + "%"
                    selectClothDetail.text = getCloth(weatherArr[0].temp)

                    // 리사이클러 뷰에 데이터 연결
                    weatherRecyclerView.adapter = WeatherAdapter(weatherArr.sliceArray(1..5))

                }
            }

            // 응답 실패 시
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
//                val tvError = findViewById<TextView>(R.id.tvError)
//                tvError.text = "api fail : " +  t.message.toString() + "\n 다시 시도해주세요."
//                tvError.visibility = View.VISIBLE
                Log.d("api fail", t.message.toString())
            }
        })
    }

    fun getSky(sky: String) : String {
        return when(sky) {
            "1" -> "맑음"
            "3" -> "구름 많음"
            "4" -> "흐림"
            else -> "오류"
        }
    }

    fun getCloth(temp: String) : String {
        var tempSystem : Int = temp.toInt()
        return when (tempSystem) {
            in 5..8 -> "히트, 코트, 니트, 청바지, 레깅스"
            in 9..11 -> "자켓, 트렌치코트, 야상, 니트, 청바지, 면바지"
            in 12..16 -> "자켓, 가디건, 야상, 맨투맨, 니트, 청바지, 면바지"
            in 17..19 -> "얇은 니트, 가디건, 맨투맨, 얇은 자켓, 면바지, 청바지"
            in 20..22 -> "얇은 가디건, 긴팔티, 면바지, 청바지"
            in 23..27 -> "반팔, 얇은 셔츠, 반바지, 면바지"
            in 28..50 -> "민소매, 반팔, 반바지, 치마"
            else -> "패딩, 두꺼운 코트, 목도리, 기모제품"
        }
    }

    // 내 현재 위치의 위경도를 격자 좌표로 변환하여 해당 위치의 날씨정보 설정하기
    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        val locationClient = LocationServices.getFusedLocationProviderClient(this@Select1Activity)

        try {
            // 나의 현재 위치 요청
            val locationRequest = LocationRequest.create()
            locationRequest.run {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 60 * 1000    // 요청 간격(1초)
            }
            val locationCallback = object : LocationCallback() {
                // 요청 결과
                override fun onLocationResult(p0: LocationResult) {
                    p0.let {
                        for (location in it.locations) {


                            // 현재 위치의 위경도를 격자 좌표로 변환
                            curPoint = WeatherDetail().dfsXyConv(location.latitude, location.longitude)

                            // nx, ny지점의 날씨 가져와서 설정하기
                            setWeather(curPoint!!.x, curPoint!!.y)
                        }
                    }
                }
            }

            // 내 위치 실시간으로 감지
            Looper.myLooper()?.let {
                locationClient.requestLocationUpdates(locationRequest, locationCallback,
                    it)
            }


        } catch (e : SecurityException) {
            e.printStackTrace()
        }
    }
}