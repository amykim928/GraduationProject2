package com.example.graduationproject.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graduationproject.ClosetDetailActivity
import com.example.graduationproject.R
import com.example.graduationproject.dataset.closetData
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


private fun stringToBitmap(encodedString: String): Bitmap {
    Log.d("check stringBitmap : ",encodedString.length.toString())
    val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)

    return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
}

class ClosetRecyclerAdapter(private val context: Context) : RecyclerView.Adapter<ClosetRecyclerAdapter.ItemViewHolder>(){

    var closetList = mutableListOf<closetData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.closet_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(closetList[position])
    }

    override fun getItemCount(): Int {
        return closetList.size
    }

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val closetclothImage = itemView.findViewById<ImageView>(R.id.closetclothImage)
        lateinit var bitmap:Bitmap
        fun bind(closetData: closetData){
            if(closetData.img_base64.length>1000){
                bitmap= stringToBitmap(closetData.img_base64)
                Glide.with(itemView).load(bitmap).into(closetclothImage)
                closetclothImage.setOnClickListener{
                    val storage: File = context.cacheDir
                    val fileName: String = "cropped.jpg"
                    val tempFile = File(storage, fileName)
                    try {
                        // 자동으로 빈 파일을 생성합니다.
                        tempFile.createNewFile()
                        val out = FileOutputStream(tempFile)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)

                        out.close()
                    } catch (e: FileNotFoundException) {
                        Log.e("MyTag", "FileNotFoundException : " + e.message)
                    } catch (e: IOException) {
                        Log.e("MyTag", "IOException : " + e.message)
                    }
                    Intent(context, ClosetDetailActivity::class.java).apply {
                        putExtra("img_url", closetData.img_url)
                        putExtra("category_id", closetData.category_id)
                        putExtra("style", closetData.style)
                        putExtra("img_base","exist")
                    }.run { context.startActivity(this) }
                }

            }else{
                Glide.with(itemView).load(closetData.img_url).into(closetclothImage)
                closetclothImage.setOnClickListener{
                    Intent(context, ClosetDetailActivity::class.java).apply {
                        putExtra("img_url", closetData.img_url)
                        putExtra("category_id", closetData.category_id)
                        putExtra("style", closetData.style)
                        putExtra("img_base", "null")
                    }.run { context.startActivity(this) }
                }

            }

        }
    }

    fun saveImg(){

    }
}

