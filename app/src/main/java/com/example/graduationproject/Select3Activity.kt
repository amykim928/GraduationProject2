package com.example.graduationproject

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.databinding.ActivitySelect3Binding

class Select3Activity: AppCompatActivity() {
    lateinit var select3Binding: ActivitySelect3Binding
    var exampleNumber=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        select3Binding=ActivitySelect3Binding.inflate(layoutInflater)
        setContentView(select3Binding.root)


        select3Binding.leftButton.setOnClickListener {
            Log.i("press left","press left")
            exampleNumber-=1
            if(exampleNumber==-1){
                exampleNumber=2
            }
            when(exampleNumber){
                0->{
                    select3Binding.example1View.setImageResource(R.drawable.example0)
                    select3Binding.textView3.text="\n\n저희 앱을 사용하시면, 아래 스타일에 따라 앱을 추천 받을 수 있어요!"

                }
                1->{
                    select3Binding.example1View.setImageResource(R.drawable.example1)
                    select3Binding.textView3.setText("\n\n옷장에서 옷을 누르면, 옷을 추천 받거나 옷을 지울 수 있어요")
                }
                2->{
                    select3Binding.example1View.setImageResource(R.drawable.example2)
                    select3Binding.textView3.setText("\n\n스타일과 옷의 카테고리를 확인하고, 옷을 옷장에서 추천받아봐요!")
                }
            }
        }

        select3Binding.rightButton.setOnClickListener {
            Log.i("press right","press right")
            exampleNumber+=1
            if(exampleNumber==3){
                exampleNumber=0
            }
            when(exampleNumber){
                0->{
                    select3Binding.example1View.setImageResource(R.drawable.example0)
                    select3Binding.textView3.setText("\n\n저희 앱을 사용하시면, 아래 스타일에 따라 앱을 추천 받을 수 있어요!")
                }
                1->{
                    select3Binding.example1View.setImageResource(R.drawable.example1)
                    select3Binding.textView3.setText(
                            "\n\n옷장에서 옷을 누르면, 옷을 추천 받거나 옷을 지울 수 있어요")
                }
                2->{
                    select3Binding.example1View.setImageResource(R.drawable.example2)
                    select3Binding.textView3.setText("\n\n스타일과 옷의 카테고리를 확인하고, 옷을 옷장에서 추천받아봐요!")
                }
            }
        }

    }
}