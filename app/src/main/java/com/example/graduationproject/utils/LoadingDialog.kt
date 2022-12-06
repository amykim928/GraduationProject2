package com.example.graduationproject.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.example.graduationproject.R

//44번째 줄 에서 사용 & 45, 79번째 줄에서 dialog 보여주고 종료시키는 코드와 함께 사용
class LoadingDialog(context: Context) : Dialog(context){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_loading)

        // 취소 불가능
        setCancelable(false)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }
}