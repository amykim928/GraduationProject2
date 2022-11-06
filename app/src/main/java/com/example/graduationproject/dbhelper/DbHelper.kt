package com.example.graduationproject.dbhelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.graduationproject.dataset.clothData
import com.example.graduationproject.dbhelper.DbHelper.Companion.id


class DbHelper(context: Context?) : SQLiteOpenHelper(context,DATABASE_NAME,null, DATABASE_VERSION) {
    companion object{
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "USER_AUTH"

        // Table
        const val TABLE_NAME = "clothes"
        const val id = "id"
        const val brand_id = "brand_id"
        const val category_id = "category_id"
        const val cl_intro = "cl_intro"
        const val cl_pd_num = "cl_pd_num"
        const val color = "cl_pd_num"
        const val setId = "cl_pd_num"
        const val img_url = "cl_pd_num"
        const val style = "cl_pd_num"

    }
    fun getCloth(cloth: clothData){
        Log.i("tag : ","DataBase Test2")

        val values = ContentValues()
        val db = this.writableDatabase
        values.put(brand_id,cloth.brand_id)
        values.put(category_id,cloth.category_id)
        values.put(cl_intro,cloth.cl_intro)
        values.put(cl_pd_num,cloth.cl_pd_num)
        values.put(color,cloth.color)
        values.put(setId,cloth.id)
        values.put(img_url,cloth.img_url)
        values.put(style,cloth.style)
        db.insert(TABLE_NAME, null, values)
        db.close()

        val query = "SELECT * FROM clothes;"
        val db2=this.readableDatabase
        var cursor = db2.rawQuery(query, null)
        Log.i("tag 2 num database:", cursor.count.toString())
        db2.close()
        cursor.close()
    }
    override fun onCreate(db: SQLiteDatabase) {
        var sql: String =
            "CREATE TABLE if not exists clothes(id integer primary key autoincrement," +
                    "brand_id integer," +
                    "category_id integer," +
                    "cl_intro TEXT," +
                    "cl_pd_num TEXT," +
                    "color TEXT, " +
                    "setId integer," +
                    "img_url TEXT," +
                    "style TEXT)";

        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql: String = "DROP TABLE if exists clothes"
        db.execSQL(sql)
        onCreate(db)
    }
    //테스트용입니다.


}