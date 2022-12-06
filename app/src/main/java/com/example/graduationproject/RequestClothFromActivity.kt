package com.example.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.graduationproject.databinding.ActivityRequestClothFromBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RequestClothFromActivity : AppCompatActivity() {
    lateinit var binding :ActivityRequestClothFromBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRequestClothFromBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.requestButton.setOnClickListener {
            val clothID=binding.clothNumberEdit.text.toString()
            val requests=binding.requestEdit.text.toString()
            val data = hashMapOf(
                "clothID" to clothID,
                "requests" to requests
            )
            val db = Firebase.firestore
            db.collection("clothRequest").add(data)
                .addOnSuccessListener{
                    Toast.makeText(this, "요청했습니다!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    // 실패할 경우
                    Toast.makeText(this, "요청에 실패했습니다!", Toast.LENGTH_SHORT).show()
                    Log.w("saveFirebaseButton", "Error getting documents: $exception")
                }
            finish()
        }


    }
}