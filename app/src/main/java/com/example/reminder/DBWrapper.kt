package com.example.reminder

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.util.*

class DBWrapper(val context: Context){
    val helper = DBHelper(context) // db and table available
    val db : SQLiteDatabase = helper.writableDatabase

    fun saveReminder(task:String,desc:String,time: Long,date : String) :Long{

        val cValues = ContentValues()
        cValues.put(DBHelper.CLM_TASK,task)
        cValues.put(DBHelper.CLM_DESC,desc)
        cValues.put(DBHelper.CLM_TIME,time)
        cValues.put(DBHelper.CLM_DATE,date)

        return db.insert(DBHelper.TABLE_NAME,null,cValues)

    }
    fun retrieveReminder() : Cursor {
        val clms = arrayOf(DBHelper.CLM_TASK,DBHelper.CLM_DESC,DBHelper.CLM_TIME,DBHelper.CLM_DATE)
        return db.query(DBHelper.TABLE_NAME,clms,
                null,null,null,null,null)
    }
    fun deleteDB(task:String){
        val args= arrayOf(task)
        db.delete(DBHelper.TABLE_NAME,"${DBHelper.CLM_TASK}= ?",args)
    }
}