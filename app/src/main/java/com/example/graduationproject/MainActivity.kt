package com.example.graduationproject

import android.Manifest.permission.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.graduationproject.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private val fl: FrameLayout by lazy{
        findViewById(R.id.frameLayout)
    }

//    private val home = HomeFragment()
//    private val search = SearchFragment()
//    private val add = AddFragment()
//    private val closet = ClosetFragment()
//    private val setting = SettingFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permission = ContextCompat.checkSelfPermission(
            this,CAMERA
        )
        val permission2 = ContextCompat.checkSelfPermission(
            this, READ_EXTERNAL_STORAGE
        )
        val permission3 =ContextCompat.checkSelfPermission(
            this, WRITE_EXTERNAL_STORAGE
        )

        val intentBackBtn=intent.getIntExtra("fragment_id",-1)
        val bn_ = findViewById<BottomNavigationView>(R.id.bn_)

        bn_.setOnItemSelectedListener { item ->
            changeFragment(
                when(item.itemId){
                    R.id.menu_home -> HomeFragment()
                    R.id.menu_search -> SearchFragment()
                    R.id.menu_add -> AddFragment()
                    R.id.menu_closet -> ClosetFragment()
                    else -> SettingFragment()
                }
            )
            true

        }
        when(intentBackBtn){
            -1 -> {
                HomeFragment()
                bn_.selectedItemId = R.id.menu_home
            }
            0 -> {
                HomeFragment()
                bn_.selectedItemId = R.id.menu_home
            }
            1->{
                SearchFragment()
                bn_.selectedItemId = R.id.menu_search
            }
            2 ->{
                AddFragment()
                bn_.selectedItemId = R.id.menu_add
            }
            3 -> {
                ClosetFragment()
                bn_.selectedItemId = R.id.menu_closet
            }
            else ->{
                SettingFragment()
                bn_.selectedItemId = R.id.menu_closet
            }
        }


        if (permission == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED || permission3 == PackageManager.PERMISSION_DENIED) {
            // 마쉬멜로우 이상버전부터 권한을 물어본다
            // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
            requestPermissions(
                arrayOf(CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE),
                1000
            )
            return
        }



    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==1000){
            var check_result = true

            // 모든 퍼미션을 허용했는지 체크
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            // 권한 체크에 동의를 하지 않으면 안드로이드 종료
            if (check_result) {
            } else {
                finish()
            }
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
    }
}