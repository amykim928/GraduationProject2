package com.example.graduationproject.adapters

import com.example.graduationproject.dataset.WeatherModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.WeatherDetail

class WeatherAdapter (var items : Array<WeatherModel>) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    // 뷰 홀더 만들어서 반환, 뷰릐 레이아웃은 list_item_weather.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_weather, parent, false)
        return ViewHolder(itemView)
    }

    // 전달받은 위치의 아이템 연결
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    // 아이템 갯수 리턴
    override fun getItemCount() = items.count()

    // 뷰 홀더 설정
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun setItem(item : WeatherModel) {
            val tvTime = itemView.findViewById<TextView>(R.id.tvTime)           //시각
            val imgWeather = itemView.findViewById<ImageView>(R.id.imgWeather)  //날씨 이미지
            val tvHumidity = itemView.findViewById<TextView>(R.id.tvHumidity)   //습도
            val tvTemp = itemView.findViewById<TextView>(R.id.tvTemp)           //온도
            //val tvRecommends = itemView.findViewById<TextView>(R.id.tvRecommends)   // 옷 추천

            tvTime.text = getTime(item.fcstTime)
            imgWeather.setImageResource(WeatherDetail().getRainImage(item.fcstTime, item.rainType, item.sky))
            tvHumidity.text = item.humidity + "%"
            tvTemp.text = item.temp + "°"
            //tvRecommends.text = getRecommends(item.temp.toInt())
        }
    }

    fun getTime(factTime : String): String {
        if(factTime != "지금"){
            var hourSystem : Int = factTime.toInt()
            var hourSystemString = ""


            if(hourSystem == 0){
                return "오전 12시"
            }else if(hourSystem > 2100){
                hourSystem -= 1200
                hourSystemString = hourSystem.toString()
                return "오후 ${hourSystemString[0]}${hourSystemString[1]}시"


            }else if(hourSystem == 1200){
                return "오후 12시"
            } else if(hourSystem > 1200){
                hourSystem -= 1200
                hourSystemString = hourSystem.toString()
                return "오후 ${hourSystemString[0]}시"

            }

            else if(hourSystem >= 1000){
                hourSystemString = hourSystem.toString()

                return "오전 ${hourSystemString[0]}${hourSystemString[1]}시"
            }else{

                hourSystemString = hourSystem.toString()

                return "오전 ${hourSystemString[0]}시"

            }

        }else{
            return factTime
        }


    }

    // 옷 추천
    fun getRecommends(temp : Int) : String{
        return when (temp) {
            in 5..8 -> "울 코트, 가죽 옷, 기모"
            in 9..11 -> "트렌치 코트, 야상, 점퍼"
            in 12..16 -> "자켓, 가디건, 청자켓"
            in 17..19 -> "니트, 맨투맨, 후드, 긴바지"
            in 20..22 -> "블라우스, 긴팔 티, 슬랙스"
            in 23..27 -> "얇은 셔츠, 반바지, 면바지"
            in 28..50 -> "민소매, 반바지, 린넨 옷"
            else -> "패딩, 누빔 옷, 목도리"
        }
    }
}