package com.calc

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.calc.historyDB.CalculationHistoryDBHelper
import com.calc.historyDB.HistoryDAO
import com.calc.ui.*
import com.example.calc.R
import kotlinx.android.synthetic.main.calc_sheet.*
import kotlinx.coroutines.launch


class CalcActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: CalcAdapter
    private lateinit var calcManager: RecyclerView.LayoutManager
    private lateinit var calcDecorator: RecyclerView.ItemDecoration
    private lateinit var calcSheetBehavior: CalcSheetBehavior<View>
    private lateinit var mainToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var linearLayout: LinearLayout
    private lateinit var motionLayout: CustomMotionLayout
    private lateinit var text1: TextView
    private lateinit var text2: TextView
    private lateinit var viewModel: CalcViewModel





    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calc)
        val button = findViewById<Button>(R.id.btnc)
        val toolbar: com.google.android.material.appbar.MaterialToolbar = findViewById(R.id.toolbar)

        val factory = CalcViewModelFactory(CalcRepository(HistoryDAO(CalculationHistoryDBHelper(applicationContext))))
        viewModel = ViewModelProvider(this, factory).get(CalcViewModel::class.java)
        viewModel.getHistoryData().observe(this, Observer {
            viewAdapter.historyDataSet = it
            viewAdapter.notifyDataSetChanged()})
        viewModel.getCurrentExpressionData().observe(this, Observer {
            viewAdapter.expressionCurrent = it
            viewAdapter.notifyDataSetChanged()
            refresh()})

        linearLayout = findViewById(R.id.linear_main)
        val foreground = getDrawable(R.color.foreground)
        linearLayout.foreground = foreground
        linearLayout.foreground.alpha = 0



        mainToolbar = findViewById(R.id.main_toolbar1)

        toolbar.navigationIcon?.colorFilter = PorterDuffColorFilter(getColor(R.color.dateTextColor), PorterDuff.Mode.MULTIPLY)
        toolbar.overflowIcon?.colorFilter = PorterDuffColorFilter(getColor(R.color.dateTextColor), PorterDuff.Mode.MULTIPLY)
        mainToolbar.overflowIcon?.colorFilter = PorterDuffColorFilter(getColor(R.color.mainToolbarColor), PorterDuff.Mode.MULTIPLY)
        mainToolbar.menu.findItem(R.menu.main_menu)
        button.setOnLongClickListener{
            motionLayout.setTransition(R.id.collapsed_empty, R.id.expanded_empty)
            motionLayout.progress = 0.0f
            viewModel.clearCurrent()
            button.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            true
        }
        toolbar.setNavigationOnClickListener {
            recyclerView.scrollToPosition(viewAdapter.itemCount - 1)
            calcSheetBehavior.state = CalcSheetBehavior.State.COLLAPSED }







        calcSheetBehavior = CalcSheetBehavior<View>().from(calc_sheet)
        viewAdapter = CalcAdapter()
        viewAdapter.expressionCurrent = viewModel.getCurrentExpressionData().value!!
        calcDecorator = CalcItemDecorator(this)
        text1 = findViewById<TextView>(R.id.textView4)
        text2 = findViewById<TextView>(R.id.textView5)



        motionLayout = findViewById(R.id.calc_sheet)

        calcSheetBehavior.addOnSlideListener { _, relativeSheetPosition ->
            linearLayout.foreground.alpha = 255*relativeSheetPosition.toInt()/100

        }


        calcManager = CalcLayoutManager(this)

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            setHasFixedSize(false)
            layoutManager = calcManager
            adapter = viewAdapter
            addItemDecoration(calcDecorator)
            recycledViewPool.setMaxRecycledViews(currentExpression, 1)



        }
        refresh()



        

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



//        motionLayout.setTransition(R.id.collapsed_expression, R.id.expanded_expression)

        viewAdapter.notifyDataSetChanged()
        motionLayout.setTransition(R.id.collapsed_expression, R.id.expanded_expression)
        motionLayout.progress = 0.0f
        viewModel.onButtonClicked((view as Button).text.toString())





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
//        recyclerView.editText.textSize = 45.0f - 10.0f*calcShitBehavior.relativeSheetPosition.toInt()/100
        viewModel.onCClicked()
    }


    //Считалка
    fun onEq (view: View) = lifecycleScope.launch  {
//        try {
//            dispVal.setText(Calculator().calculate(dispVal.text.toString()))
//            dispVal.setSelection(dispVal.text.length)
//        } catch (exception: IncorrectExpressionException) {
//            Toast.makeText(applicationContext, exception.reason, Toast.LENGTH_SHORT).show()
//        }


        motionLayout.setTransition(R.id.collapsed_expression, R.id.calculate)
        motionLayout.transitionToEnd()
        motionLayout.awaitTransitionComplete(R.id.calculate)
        viewModel.onCalculateClicked()
        refresh()
        motionLayout.progress = 0.0f
        motionLayout.setTransition(R.id.collapsed_expression, R.id.expanded_expression)



    }

    fun openHistory(menuItem: MenuItem) {
        calcSheetBehavior.state = CalcSheetBehavior.State.EXPANDED
    }

    fun clearHistory(menuItem: MenuItem) {
        viewModel.clearHistory()
    }

    fun radDeg(view: View) {
        val button = view as? Button
        if (button?.text == "DEG") {
            button.text  = "RAD"
            viewModel.setDegree(true)
        } else {
            button?.text  = "DEG"
            viewModel.setDegree(false)
        }

    }

    private fun refresh(){
        text1.text = viewModel.getCurrentExpressionData().value?.expression ?: ""
        text2.text = viewModel.getCurrentExpressionData().value?.value ?: ""
    }

}


