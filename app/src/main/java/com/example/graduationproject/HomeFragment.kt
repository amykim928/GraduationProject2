package com.example.graduationproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer

class HomeFragment : Fragment() {
    lateinit var mainActivity: MainActivity

    //Fragment의 생명주기 시작
    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val button1 = view.findViewById<ImageButton>(R.id.button1)
        val button2 = view.findViewById<ImageButton>(R.id.button2)
        val button3 = view.findViewById<ImageButton>(R.id.button3)
        button1.setOnClickListener {
            val intent = Intent(context, Select1Activity::class.java)
            startActivity(intent)
        }
        button2.setOnClickListener {
            val intent = Intent(context, Select2Activity::class.java)
            startActivity(intent)
        }
        button3.setOnClickListener {
            val intent = Intent(context, Select3Activity::class.java)
            startActivity(intent)
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        /*mainActivity.findViewById<Button>(R.id.button).setOnClickListener{
            Toast.makeText(mainActivity, "ddd", Toast.LENGTH_LONG).show()
            //넘어갈 화면
        }*/
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }
}
