package com.example.reminder
////
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.activity_your_reminders.*
import kotlin.collections.List

class yourReminders : AppCompatActivity() {

    var listOfReminders = mutableListOf<Reminder>()

    lateinit var adapter :ReminderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_reminders)


        adapter = ReminderAdapter(this,R.layout.student_list_item,listOfReminders)
        list.adapter=adapter
        //getting data from Add.kt
        //val intent = intent

        //listOfReminders.add(Reminder("default","default",00.00))

        //getting data from DB
        val wrapper = DBWrapper(this)
        val cursor =wrapper.retrieveReminder()
        if(cursor.count>0){
            cursor.moveToFirst()
            do{
                val task = cursor.getString(cursor.getColumnIndex(DBHelper.CLM_TASK))
                val desc = cursor.getString(cursor.getColumnIndex(DBHelper.CLM_DESC))
                val time = cursor.getString(cursor.getColumnIndex(DBHelper.CLM_TIME)).toLong()
                val date = cursor.getString(cursor.getColumnIndex(DBHelper.CLM_DATE))
                listOfReminders.add(Reminder(task,desc,time,date))
            }while(cursor.moveToNext())
        }
        else
            Toast.makeText(this,"Please add reminders",Toast.LENGTH_LONG).show()

        //getting data from intent
//        if(intent.hasExtra("Task")){
//            task =intent.getStringExtra("Task").toString()
//        }
//        if(intent.hasExtra("Description")){
//            desc = intent.getStringExtra("Description").toString()
//        }
//        if(intent.hasExtra("Time")){
//            time=intent.getDoubleExtra("Time",0.0) .toDouble()
//        }
        //listOfReminders.add(Reminder(task,desc,time))
        //list.onItemClickListener= this
        registerForContextMenu(list)
    }
    val MENU_DEL =1
    val MENU_ED =2
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu?.add(0,MENU_DEL,0,"Delete")
        menu?.add(0,MENU_ED,0,"Edit")
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val rem = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val taskToDelete = listOfReminders.removeAt(rem.position)
        val wrapper = DBWrapper(this)
        when(item.itemId){
            MENU_DEL ->{

                wrapper.deleteDB(taskToDelete.task)
                Toast.makeText(this,"Deleted from DB",Toast.LENGTH_LONG).show()
                adapter?.notifyDataSetChanged()
            }
            MENU_ED ->{
                //this will delete the existing reminder and redirect you to add activity
                //Toast.makeText(this,"Edit is not yet implemented",Toast.LENGTH_LONG).show()
                wrapper.deleteDB(taskToDelete.task)
                val Rintent = Intent(this,Add::class.java)
                startActivity(Rintent)
            }
        }
        return super.onContextItemSelected(item)
    }

//    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        //onCreateContextMenu(list,)
//
//    }



    fun onClickHome(view:View)
    {
        val homeIntent = Intent(this,MainActivity::class.java)
        startActivity(homeIntent)
    }

}
 data class Reminder(val task:String, val desc:String, val time:Long,val date:String)
//
class ReminderAdapter(context: Context, val layoutRes:Int, val data:List<Reminder>):ArrayAdapter<Reminder>(context,layoutRes,data)
{
    override fun getItem(position: Int): Reminder? {
        return data[position]
    }

    //getView is used to get a layout.xml
    //executed no of times as number of elements in the array
    //create view -> bind the data to it
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val reminder = getItem(position)
        //view creation
        //checking if the view is already created, if created then not created again else create again
        val view = convertView?:LayoutInflater.from(context).inflate(layoutRes,null)

        //bind data appropriately
        var hours:Int
        var totalTime = (reminder?.time)?.toInt()!!
        hours=(totalTime/(60*60*1000)).toInt()
        totalTime=totalTime-(hours*60*60*1000)
        var min = totalTime/(60*1000)


        val task = view.findViewById<TextView>(R.id.taskL)
        val desc = view.findViewById<TextView>(R.id.descL)
        val time = view.findViewById<TextView>(R.id.timeL)
        task.text="${reminder?.task}"
        desc.text = "${reminder?.desc}"

        time.text = "${hours}:${min} ${reminder?.date}"
        return view
    }


}