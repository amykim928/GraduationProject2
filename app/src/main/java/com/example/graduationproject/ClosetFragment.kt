package com.example.graduationproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.dataset.closetData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


//import kotlinx.android.synthetic.main.fragment_closet.*

private var closetList = mutableListOf<closetData>()
lateinit var closetRecycleViewAdapter: ClosetRecyclerAdapter

class ClosetFragment: Fragment(){
    //private var adapter: RecyclerView.Adapter<ClosetRecyclerAdapter.ViewHolder>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.fragment_closet, container, false)

        val closetResultView = view?.findViewById<RecyclerView>(R.id.closetResultView)
        val emptyClosetResultView = view?.findViewById<TextView>(R.id.emptyClosetResultView)
        closetResultView?.addItemDecoration(VerticalItemDecorator(20))
        closetResultView?.addItemDecoration(HorizontalItemDecorator(20))
        closetRecycleViewAdapter = context?.let { ClosetRecyclerAdapter(it) }!!

        closetResultView?.adapter = closetRecycleViewAdapter

//        val recyclerView1 = view?.findViewById<RecyclerView>(R.id.closetResultView)
//
//        recyclerView1?.adapter = context?.let { RecycleViewAdapter(it) }

        val db = Firebase.firestore
        db.collection("closetData") //파이어베이스
            .get() //필드에 해당하는 데이터 가져오기
            .addOnSuccessListener { result -> //성공시
                closetList.clear()
                if (result.isEmpty){
                    closetResultView?.setVisibility(View.GONE);
                    emptyClosetResultView?.setVisibility(View.VISIBLE);
                }
                else {
                    for(doc in result) {

                        val category=doc.data.get("category_id") as String
                        val img_url=doc.data.get("img_url") as String
                        val style =doc.data.get("style") as String

                        Log.d("tag category_check: ", category)
                        val img_base64=doc.data.get("img_base64 ") as String
//                        Log.d("tag img_base64: ", img_base64.length.toString())

                        closetList.add(
                            closetData(category,img_base64,img_url,style)  //이후 검색 결과 화면에서 옷을 선택한 후에 나오는 화면을 위해 데이터 가공하여 추가
                            )
                    }
                    closetRecycleViewAdapter.closetList = closetList
                    closetRecycleViewAdapter.notifyDataSetChanged() //adapter 새로고침

                    val gridLayoutManager = GridLayoutManager(context, 2) //2칸으로 나오게
                    closetResultView?.layoutManager = gridLayoutManager
                    emptyClosetResultView?.setVisibility(View.GONE)
                }
            }
            .addOnFailureListener{ exception ->
                Log.w("tag: ", "Error getting doc", exception)
            }
        return view
    }


}