package com.example.graduationproject.dataset

import java.io.Serializable
import java.util.*

data class closetData(
//    val brand_id: Long,
    val category_id:String,
    val img_base64:String,
    val cl_intro:String,
//    val cl_pd_num:String,
//    val color:String,
//    val id: Long,
    val img_url:String,
    val style: String,
//    val doc_id: String
    )
    : Serializable

