package com.example.calc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Selection
import android.text.Spannable
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    lateinit var dispVal: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dispVal = findViewById(R.id.dispVal)
        dispVal.showSoftInputOnFocus = false
        val button = findViewById<Button>(R.id.btnc)
        button.setOnLongClickListener{
            dispVal.setText("")
            button.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            true
        }

    }

    //Нажатие на кнопки цифр и операторов
    fun onDigit(view: View) {
        val currentSelection = dispVal.selectionStart
        if (dispVal.hasSelection()) {
            dispVal.setText(dispVal.text.replaceRange(dispVal.selectionStart, dispVal.selectionEnd, (view as Button).text))
            dispVal.setSelection(currentSelection + (view as Button).text.length)
        } else {
            dispVal.setText(
                dispVal.text.replaceRange(
                    dispVal.selectionStart,
                    dispVal.selectionStart,
                    (view as Button).text
                )
            )
            dispVal.setSelection(currentSelection + (view as Button).text.length)
        }
    }

    //Нажатие на кнопку С
    fun onClr(view: View) {
        val currentSelection = dispVal.selectionStart
        if (dispVal.hasSelection()) {
            dispVal.setText(dispVal.text.replaceRange(dispVal.selectionStart, dispVal.selectionEnd, ""))
            dispVal.setSelection(currentSelection)
        } else {
        if (currentSelection > 0){
            dispVal.setText(dispVal.text.replaceRange(dispVal.selectionStart-1, dispVal.selectionStart, ""))
            dispVal.setSelection(currentSelection-1)
            }
        }
    }


    //Считалка
    fun onEq (view: View) {
        dispVal.setText(Calculator().calculate(dispVal.text.toString()))
        dispVal.setSelection(dispVal.text.length)
    }
}
