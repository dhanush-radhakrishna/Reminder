package com.example.reminder

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*


class MyDialog: DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lateinit var dlg: Dialog
        val bundle =arguments
        val msg = bundle?.getString("msg")
        val b1 =bundle?.getString("b1")
        val b2 =bundle?.getString("b2")
        val type = bundle?.getInt("type")
        val parent =activity!!
        val builder = AlertDialog.Builder(parent)
        when(type) {
            1 -> {
                dlg = TimePickerDialog(parent, parent as TimePickerDialog.OnTimeSetListener,
                        12, 0, true)
            }
            2 ->{
                val current = Calendar.getInstance()
                dlg= DatePickerDialog(parent,parent as DatePickerDialog.OnDateSetListener, 2021,3,31)
            }
        }
        return dlg
    }
}