package com.example.graduationproject.dataset

import java.io.Serializable
import java.sql.Timestamp

class recentData (
    val brand_id: String,
    val category_id:String,
//    val cl_intro:String,
//    val cl_pd_num:String,
//    val color:String,
//    val id: Long,
    val img_url:String,
    val style: String
)
    : Serializable