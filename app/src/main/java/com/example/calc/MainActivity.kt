package com.example.calc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    lateinit var dispVal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dispVal = findViewById(R.id.dispVal)
        dispVal.setShowSoftInputOnFocus(false)
        val button = findViewById<Button>(R.id.btnc)
        button.setOnLongClickListener{
            dispVal.setText("")
            button.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            true
        }
    }

    //Нажатие на кнопки цифр и операторов
    fun onDigit(view: View) {
        dispVal.append((view as Button).text)
    }

    //Нажатие на кнопку С
    fun onClr(view: View) {
        dispVal.setText(dispVal.text.dropLast(1))
    }


    //Считалка
    fun onEq (view: View) {
        //var rpn = RPN(dispVal.text.toString())
        //val rpn = ShuntingYardAlgorythm(dispVal.text.toString()).makePostFix()
        //val po = eval(rpn)
        dispVal.setText(Calculator(dispVal.text.toString()).calculate())
    }
}
