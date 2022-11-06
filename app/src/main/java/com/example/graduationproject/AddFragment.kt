package com.example.graduationproject

import android.app.Activity
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.graduationproject.databinding.FragmentAddBinding
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class AddFragment: Fragment(R.layout.fragment_add) {
    private var _binding: FragmentAddBinding? =null
    private val binding get()=_binding!!
    lateinit var bitmap: Bitmap
    lateinit var bitmap2:Bitmap

    //사진 사이즈를 416으로 정하겠다는 의미입니다.
    val TF_OD_API_INPUT_SIZE = 416



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = com.example.graduationproject.databinding.FragmentAddBinding.inflate(inflater,container,false)
        val activity = activity as MainActivity


        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            val uri :Intent=it.data as Intent
            cropImage(uri.data)
            Log.d("mmm ddk", CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE.toString())

        }

        binding.cancelButton.setOnClickListener {
            binding.imageView.visibility=View.INVISIBLE
            binding.buttonLinearLayout.visibility=View.INVISIBLE
            binding.addCameraLayout.visibility=View.VISIBLE
            binding.addGalleryLayout.visibility=View.VISIBLE

        }
        binding.getClassButton.setOnClickListener {
            val storage: File = activity.cacheDir
            val fileName: String = "cropped.jpg"
            val tempFile = File(storage, fileName)
//            val fileName2: String = "uncropped.jpg"
//            val tempFile2=File(storage,fileName2)
            try {
                // 자동으로 빈 파일을 생성합니다.
                tempFile.createNewFile()
//                tempFile2.createNewFile()
                // 파일을 쓸 수 있는 스트림을 준비합니다.
                val out = FileOutputStream(tempFile)
//                val out2=FileOutputStream(tempFile2)
                // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                // 스트림 사용후 닫아줍니다.
                out.close()
            } catch (e: FileNotFoundException) {
                Log.e("MyTag", "FileNotFoundException : " + e.message)
            } catch (e: IOException) {
                Log.e("MyTag", "IOException : " + e.message)
            }


            val intent=Intent(activity,AddDataActivity::class.java)
            startActivity(intent)

        }

        binding.addCameraLayout.setOnClickListener {
            Log.i("qwer","CameraLayout")
            val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            getContent.launch(intent)
        }
        binding.addGalleryLayout.setOnClickListener {
            Log.i("qwer2","GalleryLayout")
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT;
            getContent.launch(intent)
            Log.i("qwer2","check time")

        }

        return binding.root
    }

    private fun cropImage(uri: Uri?) {
        val crop =CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)  // 크롭 위한 가이드 열어서 크롭할 이미지 받아오기
            .setCropShape(CropImageView.CropShape.RECTANGLE)            // 사각형으로 자르기
            .start(activity as MainActivity,this@AddFragment)

        // 프레그먼트에서 사용할 땐 .start(activity as 프레그먼트의 부모 Activity, this@형재 프레그먼트 이름)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            // 크롭해서 프로필 사진 설정하기
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    result.uri?.let {
                        // 이미지 파일 읽어와서 설정하기

                        bitmap = BitmapFactory.decodeStream(activity?.contentResolver!!.openInputStream(result.uri!!))
                        bitmap=Bitmap.createScaledBitmap(bitmap,416,416,true)
                        binding.imageView.setImageBitmap(bitmap)
                        binding.addCameraLayout.visibility=View.GONE
                        binding.addGalleryLayout.visibility=View.GONE
                        binding.buttonLinearLayout.visibility=View.VISIBLE

                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        _binding=null;
        super.onDestroyView()

    }

}