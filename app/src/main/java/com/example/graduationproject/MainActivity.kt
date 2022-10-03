package com.example.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.fragment.app.Fragment
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

    //lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bn_ = findViewById<BottomNavigationView>(R.id.bn_)
        //supportFragmentManager.beginTransaction().add(fl.id, HomeFragment()).commit()

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
        bn_.selectedItemId = R.id.menu_home
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
    }
}