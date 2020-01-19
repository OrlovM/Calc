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



    fun eval(rpn: List<String>): String {
        val stack = Stack<Double>()
        stack.push(0.0)
        rpn.forEach { current ->
            when (current) {
                "+" -> runBinary(stack) { x, y -> x + y }
                "-" -> runBinary(stack) { x, y -> x - y }
                "*" -> runBinary(stack) { x, y -> x * y }
                "/" -> runBinary(stack) { x, y -> x / y }
                "^" -> runBinary(stack) { x, y -> (Math.pow (x, y))}
                else -> stack.push(current.toDouble())
            }
        }
        return stack.peek().toString()
    }

    fun runBinary(stack: Stack<Double>, operator: (Double, Double) -> Double) {
        val y = stack.pop()
        val x = stack.pop()
        val result = operator(x, y)
        stack.push(result)
    }
    //Считалка
    fun onEq (view: View) {
        //var rpn = RPN(dispVal.text.toString())
        val rpn = ShuntingYardAlgorythm().makePostFix(dispVal.text.toString())
        val po = eval(rpn)
        dispVal.setText(po)
    }
}
