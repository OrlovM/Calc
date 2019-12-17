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

    //Функция возвращающая полученное инфиксное выражение в обратной польской нотации
    fun RPN (instring: String): ArrayList<String> {
        val exlist = ArrayList<String>() //Выходная строка
        val stack = Stack<Char>() //Стек операторов
        val tbe = instring.length-1
        var digit = false   //Тру, если последний символ из строки число
        fun prior (x: Char): Int {      //Приоритет операторов
            var pr: Int = 0
            when (x) {
                '(' -> pr = 1
                '+', '-' -> pr = 2
                '*', '/'  -> pr = 3
                '^' -> pr = 4
            }
            return pr
        }
        for (i in 0..tbe) {
            val symbol = instring[i]
            var unarMin: Boolean                                       //Проверяем на унарный минус
            var lastBrc = i > 1 && instring[i-1] == ')'
            var unarCond = i < tbe && !digit && (instring[i+1] != '(') //Условия унарного минуса(кажется костыльная конструкция получилась)
            unarMin = instring[i] == '-' && unarCond && !lastBrc
            if (symbol in '0'..'9' || symbol =='.' || unarMin) {       //Символы из которых строится число
                if (digit) {                                           //Если предыдущий символ был цифрой, то новый символ добавляем в его элемент списка
                    var asd: String = exlist.last()
                    asd = "$asd$symbol"
                    exlist.removeAt(exlist.size-1)
                    exlist.add(asd)
                }
                else {
                    exlist.add(symbol.toString())                       //Добавляем новый элемент списка
                }
                digit = true
            }
            else {
                if (stack.empty() || symbol =='(') {
                    stack.push(symbol)
                }
                else {
                    if (symbol == ')') {
                        while (prior(stack.peek()) > 1) {
                            exlist.add(stack.pop().toString())
                        }
                        stack.pop()
                    }
                    else {
                        var pr = prior(stack.peek())
                        while (prior(symbol) <= pr) {
                                exlist.add(stack.pop().toString())
                                if (stack.empty()) {
                                    pr = 0
                                }
                                else {
                                    pr = prior(stack.peek())
                                }
                        }
                        stack.push(symbol)
                    }
                }
                digit = false
            }
        }
        while (!stack.empty()) {
            exlist.add(stack.pop().toString())
        }
        return exlist
    }

    fun eval(rpn: List<String>): String {
        val stack = Stack<Float>()
        stack.push(0f)
        rpn.forEach { current ->
            when (current) {
                "+" -> runBinary(stack) { x, y -> x + y }
                "-" -> runBinary(stack) { x, y -> x - y }
                "*" -> runBinary(stack) { x, y -> x * y }
                "/" -> runBinary(stack) { x, y -> x / y }
                "^" -> runBinary(stack) { x, y -> (Math.pow (x.toDouble(), y.toDouble())).toFloat()}
                else -> stack.push(current.toFloat())
            }
        }
        return stack.peek().toString()
    }

    fun runBinary(stack: Stack<Float>, operator: (Float, Float) -> Float) {
        val y = stack.pop()
        val x = stack.pop()
        val result = operator(x, y)
        stack.push(result)
    }
    //Считалка
    fun onEq (view: View) {
        var rpn = RPN(dispVal.text.toString())
        var po = eval(rpn)
        dispVal.setText(po)
    }
}
