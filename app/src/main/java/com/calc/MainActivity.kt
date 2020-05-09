package com.calc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.calc.R
import androidx.recyclerview.widget.RecyclerView
import com.calc.Calculator.Calculator
import com.calc.Calculator.IncorrectExpressionException
import com.calc.ui.*
import kotlinx.android.synthetic.main.bottom_shit2.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var calcManager: RecyclerView.LayoutManager
    private lateinit var calcDecorator: RecyclerView.ItemDecoration
    lateinit var calcShitBehavior: CalcSheetBehavior<View>

    val ab = Expression("321+545","1", GregorianCalendar(2019,11,2))
    val bc = Expression("2+2","2", GregorianCalendar(2019,11,3))
    val cd = Expression("12354*45354","3", GregorianCalendar(2020,3,23))
    val de = Expression("4+2/3*4","4", GregorianCalendar(2020,3,29))
    val ef = Expression("4(23)-32(4-1)","5", GregorianCalendar(2020,4,2))
    val fg = Expression("666+666","6", GregorianCalendar(2020,4,5))
    val gh = Expression("123-333333","7", GregorianCalendar(2020, 5, 3))
    var hi = Expression("123","8", GregorianCalendar(2020, 6 ,11))
    var hi2 = Expression("1233","9", GregorianCalendar(2020, 6 ,12))
    var hi3 = Expression("1232","10", GregorianCalendar(2019,11,2))
    var hi4 = Expression("1231","11", GregorianCalendar(2019,11,2))
    var hi5 = Expression("12377","12", GregorianCalendar(2019,11,2))
    val a1 = Expression("321+545","13", GregorianCalendar(2019,11,2))
    val a2 = Expression("2+2","14", GregorianCalendar(2019,11,3))
    val a3 = Expression("12354*45354","15", GregorianCalendar(2020,3,23))
    val a4 = Expression("4+2/3*4","16", GregorianCalendar(2020,3,29))
    val a5 = Expression("4(23)-32(4-1)","17", GregorianCalendar(2020,4,2))
    val a6 = Expression("666+666","18", GregorianCalendar(2020,4,5))
    val a7 = Expression("123-333333","19", GregorianCalendar(2020, 5, 3))
    var a8 = Expression("123","20", GregorianCalendar(2020, 6 ,12))
    var a9 = Expression("1233","21", GregorianCalendar(2020, 6 ,12))
    var a10 = Expression("1232","22", GregorianCalendar(2020, 6 ,12))
    var a11 = Expression("1231","23", GregorianCalendar(2020, 6 ,14))
    var a12 = Expression("1254*36","4567", GregorianCalendar(2020, 6 ,15))
    var expressionDataset = arrayOf(ab, bc, cd, de, ef, fg, gh, hi, hi2, hi3 , hi4 , hi5, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        dispVal = findViewById(R.id.dispVal)
//        dispVal.showSoftInputOnFocus = false
        val button = findViewById<Button>(R.id.btnc)
        button.setOnLongClickListener{
            a12.aa = ""
            button.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            viewAdapter.notifyDataSetChanged()
            true
        }
        viewAdapter = MyAdapter(expressionDataset)
        calcDecorator = CalcItemDecorator(this)


        calcShitBehavior = CalcSheetBehavior<View>(this).from(bottom_shit2)

//        bottomShitBehavior.setCallback(callback)
        calcManager = CalcLayoutManager(this, calcShitBehavior)

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(false)

            // use a linear layout manager
            layoutManager = calcManager


            // specify an viewAdapter (see also next example)
            adapter = viewAdapter


            addItemDecoration(calcDecorator)


        }
    }

    //Нажатие на кнопки цифр и операторов
    fun onDigit(view: View) {
//        val currentSelection = dispVal.selectionStart
//        if (dispVal.hasSelection()) {
//            dispVal.setText(dispVal.text.replaceRange(dispVal.selectionStart, dispVal.selectionEnd, (view as Button).text))
//            dispVal.setSelection(currentSelection + (view).text.length)
//        } else {
//            dispVal.setText(
//                dispVal.text.replaceRange(
//                    dispVal.selectionStart,
//                    dispVal.selectionStart,
//                    (view as Button).text
//                )
//            )
//            dispVal.setSelection(currentSelection + (view).text.length)
//        }
        a12.aa = "${a12.aa}${(view as Button).text}"
        viewAdapter.notifyDataSetChanged()
    }

    //Нажатие на кнопку С
    fun onClr(view: View) {
//        val currentSelection = dispVal.selectionStart
//        if (dispVal.hasSelection()) {
//            dispVal.setText(dispVal.text.replaceRange(dispVal.selectionStart, dispVal.selectionEnd, ""))
//            dispVal.setSelection(currentSelection)
//        } else {
//        if (currentSelection > 0){
//            dispVal.setText(dispVal.text.replaceRange(dispVal.selectionStart-1, dispVal.selectionStart, ""))
//            dispVal.setSelection(currentSelection-1)
//            }
//        }

    }


    //Считалка
    fun onEq (view: View) {
//        try {
//            dispVal.setText(Calculator().calculate(dispVal.text.toString()))
//            dispVal.setSelection(dispVal.text.length)
//        } catch (exception: IncorrectExpressionException) {
//            Toast.makeText(applicationContext, exception.reason, Toast.LENGTH_SHORT).show()
//        }
        try {
            a12.aa = (Calculator().calculate(a12.aa))
//            dispVal.setSelection(dispVal.text.length)
        } catch (exception: IncorrectExpressionException) {
            Toast.makeText(applicationContext, exception.reason, Toast.LENGTH_SHORT).show()
        }
        viewAdapter.notifyDataSetChanged()
    }
}
