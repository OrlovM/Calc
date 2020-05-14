package com.calc

import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.calc.Calculator.Calculator
import com.calc.Calculator.IncorrectExpressionException
import com.calc.historyDB.HistoryManager
import com.calc.ui.*
import com.example.calc.R
import kotlinx.android.synthetic.main.bottom_shit2.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var calcManager: RecyclerView.LayoutManager
    private lateinit var calcDecorator: RecyclerView.ItemDecoration
    lateinit var calcShitBehavior: CalcSheetBehavior<View>

    val ab = Expression("321+545","1", GregorianCalendar(2019,11,2).time.time)
    val bc = Expression("2+2","2", GregorianCalendar(2019,11,3).time.time)
    val cd = Expression("12354*45354","3", GregorianCalendar(2020,3,23).time.time)
    val de = Expression("4+2/3*4","4", GregorianCalendar(2020,3,29).time.time)
    val ef = Expression("4(23)-32(4-1)","5", GregorianCalendar(2020,4,2).time.time)
    val fg = Expression("666+666","6", GregorianCalendar(2020,4,5).time.time)
    val gh = Expression("123-333333","7", GregorianCalendar(2020, 5, 3).time.time)
    var hi = Expression("123","8", GregorianCalendar(2020, 6 ,11).time.time)
    var hi2 = Expression("1233","9", GregorianCalendar(2020, 6 ,12).time.time)
    var hi3 = Expression("1232","10", GregorianCalendar(2019,11,2).time.time)
    var hi4 = Expression("1231","11", GregorianCalendar(2019,11,2).time.time)
    var hi5 = Expression("12377","12", GregorianCalendar(2019,11,2).time.time)
    val a1 = Expression("321+545","13", GregorianCalendar(2019,11,2).time.time)
    val a2 = Expression("2+2","14", GregorianCalendar(2019,11,3).time.time)
    val a3 = Expression("12354*45354","15", GregorianCalendar(2020,3,23).time.time)
    val a4 = Expression("4+2/3*4","16", GregorianCalendar(2020,3,29).time.time)
    val a5 = Expression("4(23)-32(4-1)","17", GregorianCalendar(2020,4,2).time.time)
    val a6 = Expression("666+666","18", GregorianCalendar(2020,4,5).time.time)
    val a7 = Expression("123-333333","19", GregorianCalendar(2020, 5, 3).time.time)
    var a8 = Expression("123","20", GregorianCalendar(2020, 6 ,12).time.time)
    var a9 = Expression("1233","21", GregorianCalendar(2020, 6 ,12).time.time)
    var a10 = Expression("1232","22", GregorianCalendar(2020, 6 ,12).time.time)
    var a11 = Expression("1231","23", GregorianCalendar(2010, 6 ,14).time.time)
    var a12 = Expression("1254*36","4567", GregorianCalendar(2020, 6 ,15).time.time)
    private var expressionDataset = ArrayList<Expression>()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HistoryManager.context = applicationContext
//        dispVal = findViewById(R.id.dispVal)
//        dispVal.showSoftInputOnFocus = false
        val button = findViewById<Button>(R.id.btnc)
        button.setOnLongClickListener{
            a12.expression = ""
            button.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            viewAdapter.notifyDataSetChanged()
            true
        }
        expressionDataset.addAll(HistoryManager.query())






        viewAdapter = CalcAdapter(expressionDataset)
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
//        a12.aa = "${a12.aa}${(view as Button).text}"
//        viewAdapter.notifyDataSetChanged()

//       HistoryManager.query()


        viewAdapter.notifyDataSetChanged()
        Log.i("FFD", "${HistoryManager.query().size}")

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
            a12.expression = (Calculator().calculate(a12.expression))
//            dispVal.setSelection(dispVal.text.length)
        } catch (exception: IncorrectExpressionException) {
            Toast.makeText(applicationContext, exception.reason, Toast.LENGTH_SHORT).show()
        }
        viewAdapter.notifyDataSetChanged()
    }
}
