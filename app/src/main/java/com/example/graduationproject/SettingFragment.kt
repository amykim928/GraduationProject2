package com.example.graduationproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment


class SettingFragment: Fragment(R.layout.fragment_setting) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity as MainActivity
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        val requestplz=view.findViewById<TextView>(R.id.textrequest)
        val layoutBtn = view.findViewById<LinearLayout>(R.id.addRequestLayout)
        layoutBtn.setOnClickListener {
            Log.d("check click","check")
            val intent=Intent(activity,RequestClothFromActivity::class.java)
            startActivity(intent)}
        requestplz.setOnClickListener{
            Log.d("check click","check")
            val intent=Intent(activity,RequestClothFromActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}


