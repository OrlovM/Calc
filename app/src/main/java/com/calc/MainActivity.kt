package com.calc

import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.calc.common.GodObject
import com.calc.historyDB.HistoryManager
import com.calc.ui.*
import com.example.calc.R
import kotlinx.android.synthetic.main.bottom_shit2.*


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var calcManager: RecyclerView.LayoutManager
    private lateinit var calcDecorator: RecyclerView.ItemDecoration
    lateinit var calcShitBehavior: CalcSheetBehavior<View>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HistoryManager.context = applicationContext
        val button = findViewById<Button>(R.id.btnc)
        button.setOnLongClickListener{
            GodObject.clear()
            button.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            true
        }







        viewAdapter = CalcAdapter(GodObject)
        calcDecorator = CalcItemDecorator(this)


        calcShitBehavior = CalcSheetBehavior<View>(this).from(bottom_shit2)

        GodObject.initCalcSheet(calcShitBehavior)

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

        GodObject.onButtonClicked((view as Button).text.toString())

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
        GodObject.onCClicked()

    }


    //Считалка
    fun onEq (view: View) {
//        try {
//            dispVal.setText(Calculator().calculate(dispVal.text.toString()))
//            dispVal.setSelection(dispVal.text.length)
//        } catch (exception: IncorrectExpressionException) {
//            Toast.makeText(applicationContext, exception.reason, Toast.LENGTH_SHORT).show()
//        }

        GodObject.onCalculateClicked()
    }
}
