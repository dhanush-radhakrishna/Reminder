package com.example.reminder

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    val MENU_CALL =1
    val MENU_MSG =2
    val MENU_MAIL =3

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0,MENU_CALL,1,"Call")
        menu?.add(0,MENU_MAIL,2,"Email")
        menu?.add(0,MENU_MSG,3,"SMS")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            MENU_CALL ->{
                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:12345678"))
                startActivity(callIntent)
            }
            MENU_MSG ->{
                val msgIntent = Intent(Intent.ACTION_VIEW)
                msgIntent.setData(Uri.parse("sms:"))
                startActivity(msgIntent)
            }
            MENU_MAIL ->{
                val mailIntent = Intent(Intent.ACTION_SENDTO)
                mailIntent.setData(Uri.parse("mailTo:"))
                startActivity(mailIntent)
            }
        }
        return super.onOptionsItemSelected(item)

    }






    fun onClickAdd(view: View){
        // launch add activity
        val i1= Intent(this,Add::class.java)
        startActivity(i1)
    }
    fun onClickDelete(view:View){
        //launch view activity
        val i2 = Intent(this,yourReminders::class.java)
        startActivity(i2)
    }
    fun onClickView(view:View){
        //launch view activity
        val i2 = Intent(this,yourReminders::class.java)
        startActivity(i2)
    }

}