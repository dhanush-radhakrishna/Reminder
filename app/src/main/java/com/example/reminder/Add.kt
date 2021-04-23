package com.example.reminder

import android.app.*
import android.content.*
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.android.synthetic.main.activity_add.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
val MY_REMINDER_NOTIFICATION = "com.example.reminder.action.scheduled"
class Add : AppCompatActivity(),TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val receiver =LocalReceiver()
        val filter  = IntentFilter(MY_REMINDER_NOTIFICATION)
        registerReceiver(receiver,filter)
    }
    fun onClickCancel(view: View){
        //go back
        val backIntent = Intent(this,MainActivity::class.java)
        startActivity(backIntent)
    }

    fun onClickTime(view:View){
        //show time picker
        val dlg=MyDialog()
        val bund=Bundle()
        bund.putInt("type",1)
        dlg.arguments=bund
        dlg.show(supportFragmentManager,"time")
    }
    fun onClickDate(view:View){
        //show date picker
        val dlg = MyDialog()
        val bund = Bundle()
        bund.putInt("type",2)
        dlg.arguments=bund
        dlg.show(supportFragmentManager,"date")
    }
    fun onClickAdd(view:View){
        //launch view activity
        val viewIntent = Intent(this,yourReminders::class.java)
        val task =taskEntry.text.toString()
        val description = descEntry.text.toString()

        //saving data in database
        if(task.isNotEmpty() && description.isNotEmpty()){
            val wrapper = DBWrapper(this)
            val rowid =wrapper.saveReminder(task,description,time,date)
            if(rowid.toInt()!=-1){
                Toast.makeText(this,"Data is saved in DB",Toast.LENGTH_LONG).show()
            }
            else
                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
        }
//        if(description.isNotEmpty())
//        {
//            addIntent.putExtra("Description",description)
//        }
//        addIntent.putExtra("Time",time)
        //setting alarm manager for notification
        setAlarm(task)

        setEventCalendar(task)
        startActivity(viewIntent)
        //populate the list
    }
    var time :Long=1
    var date :String=""
    var hour:Int=0
    var min:Int=0
    var yr:Int=0
    var mon:Int=0
    var day:Int=0
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
       // Toast.makeText(this,"Select time", Toast.LENGTH_LONG).show()
        time=((hourOfDay*60*60*1000)+(minute*60*1000)).toLong()
        hour=hourOfDay
        min=minute
        //time = hourOfDay.toDouble()

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            date=dayOfMonth.toString()+"/"+month.toString()+"/"+year.toString()
            yr=year
            mon=month
            day=dayOfMonth
    }
    var today = Calendar.getInstance()
    
    fun setAlarm(task:String){


        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val Ni= Intent(MY_REMINDER_NOTIFICATION)
        val pi = PendingIntent.getBroadcast(this,0,Ni,0)

        alarmManager.set(AlarmManager.RTC,time,pi)
    }
    class LocalReceiver :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context,"alarm set",Toast.LENGTH_LONG).show()
            //status bar notification for making foreground service
            val nManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder: Notification.Builder
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                val ch = NotificationChannel("test","MyChannel",
                    NotificationManager.IMPORTANCE_DEFAULT)
                nManager.createNotificationChannel(ch)
                builder = Notification.Builder(context,"test")

            }
            else
                builder = Notification.Builder(context)

            builder.setContentText("${R.id.taskEntry}")
            builder.setSmallIcon(R.drawable.ic_launcher_foreground)

            val i =Intent(context,yourReminders::class.java)
            val pi = PendingIntent.getActivity(context,0,i,0)
            builder.setContentIntent(pi)

            val notification = builder.build()
        }
    }
    fun setEventCalendar(task:String){

        val startMillis: Long = Calendar.getInstance().run {
            set(yr, mon,day , hour, min)
            timeInMillis
        }
        val endMillis: Long = Calendar.getInstance().run {
            set(yr, mon, day, hour+1, min)
            timeInMillis
        }

        val cr=contentResolver
        val cVal=ContentValues()
        cVal.put(CalendarContract.Events.DTSTART,startMillis)
        cVal.put(CalendarContract.Events.DTEND,endMillis)
        cVal.put(CalendarContract.Events.TITLE,task)
        cVal.put(CalendarContract.Events.CALENDAR_ID,3)
        cVal.put(CalendarContract.Events.EVENT_TIMEZONE,TimeZone.getDefault().id)
        cr.insert(CalendarContract.Events.CONTENT_URI,cVal)
    }
}