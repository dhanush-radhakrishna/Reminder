package com.example.reminder

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context:Context) :SQLiteOpenHelper(context,"reminder.db",null,1){

    companion object{
        val TABLE_NAME = "REMINDER"
        val CLM_TASK= "task"
        val CLM_DESC = "description"
        val CLM_TIME = "time"
        val CLM_DATE ="date"
        val TABLE_QUERY = "create table $TABLE_NAME ($CLM_TASK text,$CLM_DESC text,$CLM_TIME number,$CLM_DATE text)"
    }
    override fun onCreate(db:SQLiteDatabase?){
        try{
            db?.execSQL(TABLE_QUERY)
        }catch(e:Exception){
            Log.e("DB helper","ERROR creating  table : ${e.message}")
        }
    }
    override fun onUpgrade(db: SQLiteDatabase?,oldV:Int,newV:Int){

    }
}