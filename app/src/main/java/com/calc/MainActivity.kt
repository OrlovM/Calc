package com.calc

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.calc.common.CalcFacade
import com.calc.historyDB.HistoryManager
import com.calc.ui.*
import com.example.calc.R
import kotlinx.android.synthetic.main.calc_sheet.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var calcManager: RecyclerView.LayoutManager
    private lateinit var calcDecorator: RecyclerView.ItemDecoration
    private lateinit var calcSheetBehavior: CalcSheetBehavior<View>
    private lateinit var mainToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var linearLayout: LinearLayout
    private lateinit var motionLayout: CustomMotionLayout
    private lateinit var text1: TextView
    private lateinit var text2: TextView




    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HistoryManager.context = applicationContext
        val button = findViewById<Button>(R.id.btnc)
        val toolbar: com.google.android.material.appbar.MaterialToolbar = findViewById(R.id.toolbar)

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
            CalcFacade.kolhoz = 0
            viewAdapter.notifyDataSetChanged()
            motionLayout.setTransition(R.id.collapsed_empty, R.id.expanded_empty)
            motionLayout.progress = 0.0f
            CalcFacade.clearCurrent()
            button.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            refresh()
            true
        }
        toolbar.setNavigationOnClickListener {
            recyclerView.scrollToPosition(viewAdapter.itemCount - 1)
            calcSheetBehavior.state = CalcSheetBehavior.State.COLLAPSED }







        calcSheetBehavior = CalcSheetBehavior<View>().from(calc_sheet)
        viewAdapter = CalcAdapter(CalcFacade)
        calcDecorator = CalcItemDecorator(this)
        text1 = findViewById<TextView>(R.id.textView4)
        text2 = findViewById<TextView>(R.id.textView5)



        motionLayout = findViewById(R.id.calc_sheet)
//        motionLayout.setTransitionListener(object: MotionLayout.TransitionListener {
//
//            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
//
//            }
//
//            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
//
//            }
//
//            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
//
//            }
//
//            override fun onTransitionCompleted(p0: MotionLayout, p1: Int) {
//                val text1 = findViewById<TextView>(R.id.textView4)
//                val text2 = findViewById<TextView>(R.id.textView5)
//                if (p1 == R.id.end) {
//                    text1.visibility = View.INVISIBLE
//                    text2.visibility = View.INVISIBLE
//                }
//
//                text1.text = "asd"
//                Log.i("CALCCA", "onCompleted $p1 ${R.id.end}")
//            }
//
//        })


        calcSheetBehavior.addOnSlideListener { _, relativeSheetPosition ->
            linearLayout.foreground.alpha = 255*relativeSheetPosition.toInt()/100

        }

//        calcSheetBehavior.addOnStateChangedListener { state ->
//            val text1 = findViewById<TextView>(R.id.textView4)
//            val text2 = findViewById<TextView>(R.id.textView5)
//            if (state == CalcSheetBehavior.State.EXPANDED) {
//                text1.visibility = View.INVISIBLE
//                text2.visibility = View.INVISIBLE
//            } else { text1.visibility = View.VISIBLE
//                text2.visibility = View.VISIBLE
//
//            }
//        }



//        CalcFacade.initCalcSheet(calcSheetBehavior)


        calcManager = CalcLayoutManager(this)

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(false)

            layoutManager = calcManager
            adapter = viewAdapter
            addItemDecoration(calcDecorator)
            recycledViewPool.setMaxRecycledViews(currentExpression, 1)



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

//        motionLayout.setTransition(R.id.collapsed_expression, R.id.expanded_expression)
        CalcFacade.kolhoz = 1
        viewAdapter.notifyDataSetChanged()
        motionLayout.setTransition(R.id.collapsed_expression, R.id.expanded_expression)
        motionLayout.progress = 0.0f
        CalcFacade.onButtonClicked((view as Button).text.toString())
        refresh()




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
        CalcFacade.onCClicked()
        refresh()

//        val animator = ValueAnimator.ofFloat(200.0f, 30.0f)
//        animator.duration = 4000L
//        animator.addUpdateListener { valueAnimator -> recyclerView.editText.textSize = (valueAnimator.animatedValue as Float)
//        Log.i("CALCCALC", "${recyclerView.editText.textSize}")}
//        animator.start()
    }


    //Считалка
    fun onEq (view: View) = lifecycleScope.launch  {
//        try {
//            dispVal.setText(Calculator().calculate(dispVal.text.toString()))
//            dispVal.setSelection(dispVal.text.length)
//        } catch (exception: IncorrectExpressionException) {
//            Toast.makeText(applicationContext, exception.reason, Toast.LENGTH_SHORT).show()
//        }

        CalcFacade.onCalculateClicked()
        motionLayout.setTransition(R.id.collapsed_expression, R.id.calculate)
        motionLayout.transitionToEnd()
        motionLayout.awaitTransitionComplete(R.id.calculate)

//            motionLayout.progress = 0.0f


            refresh()
            motionLayout.progress = 0.0f
            motionLayout.setTransition(R.id.collapsed_expression, R.id.expanded_expression)
//            motionLayout.progress = 0.0f
//            refresh()


    }

    fun openHistory(menuItem: MenuItem) {
        calcSheetBehavior.state = CalcSheetBehavior.State.EXPANDED

    }

    fun clearHistory(menuItem: MenuItem) {
        CalcFacade.clearHistory()
        refresh()
    }

    fun radDeg(view: View) {
        val button = view as? Button
        if (button?.text == "DEG") {
            button.text  = "RAD"
            CalcFacade.setDegree(true)
        } else {
            button?.text  = "DEG"
            CalcFacade.setDegree(false)
        }

    }

    fun refresh(){
        text1.text = CalcFacade.currentExpression.expression
        text2.text = CalcFacade.currentExpression.value

    }

}


