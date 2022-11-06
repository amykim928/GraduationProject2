package com.example.graduationproject

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.graduationproject.databinding.FragmentAddBinding
import android.app.Activity.RESULT_OK
import android.content.Intent

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts




import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
class AddFragment: Fragment(R.layout.fragment_add) {
    private var _binding: FragmentAddBinding? =null
    private val binding get()=_binding!!
    lateinit var bitmap: Bitmap

    //사진 사이즈를 416으로 정하겠다는 의미입니다.
    val TF_OD_API_INPUT_SIZE = 416


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = com.example.graduationproject.databinding.FragmentAddBinding.inflate(inflater,container,false)
        val activity = activity as MainActivity


        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            val contentResolver1=activity.contentResolver;
//
//            bitmap= Utils.processBitmap(
//                    ImageDecoder.decodeBitmap(
//                        ImageDecoder.createSource(contentResolver1,uri!!),
//                        ImageDecoder.OnHeaderDecodedListener { decoder, info, source ->
//                            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
//                            decoder.isMutableRequired = true
//                        }),TF_OD_API_INPUT_SIZE)
            val uri :Intent=it.data as Intent
            cropImage(uri.data)
            Log.d("mmm ddk", CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE.toString())

        }
        val getCameras = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode==RESULT_OK && it.data != null){
                val extras=it.data!!.extras
                bitmap=extras?.get("data") as Bitmap

            }
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

            try {
                // 자동으로 빈 파일을 생성합니다.
                tempFile.createNewFile()
                // 파일을 쓸 수 있는 스트림을 준비합니다.
                val out = FileOutputStream(tempFile)

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
            getCameras.launch(intent)
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
}