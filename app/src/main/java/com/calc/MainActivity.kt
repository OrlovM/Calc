package com.calc

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.RecyclerView
import com.calc.common.CalcFacade
import com.calc.historyDB.HistoryManager
import com.calc.ui.*
import com.example.calc.R
import com.google.android.material.transformation.FabTransformationBehavior
import com.google.android.material.transformation.FabTransformationScrimBehavior
import kotlinx.android.synthetic.main.calc_sheet.*
import kotlinx.android.synthetic.main.current_expression_item.view.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var calcManager: RecyclerView.LayoutManager
    private lateinit var calcDecorator: RecyclerView.ItemDecoration
    private lateinit var calcShitBehavior: CalcSheetBehavior<View>
    private lateinit var mainToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var fadeInAnimation: Animation
    private lateinit var fadeOutAnimation: Animation
    private lateinit var lastCalcSheetState: CalcSheetBehavior.State
    private lateinit var linearLayout: LinearLayout





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



        mainToolbar = findViewById(R.id.main_toolbar)

        toolbar.navigationIcon?.colorFilter = PorterDuffColorFilter(getColor(R.color.dateTextColor), PorterDuff.Mode.MULTIPLY)
        toolbar.overflowIcon?.colorFilter = PorterDuffColorFilter(getColor(R.color.dateTextColor), PorterDuff.Mode.MULTIPLY)
        mainToolbar.overflowIcon?.colorFilter = PorterDuffColorFilter(getColor(R.color.mainToolbarColor), PorterDuff.Mode.MULTIPLY)
        mainToolbar.menu.findItem(R.menu.main_menu)
        button.setOnLongClickListener{
            CalcFacade.clearCurrent()
            button.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            true
        }
        toolbar.setNavigationOnClickListener {
            recyclerView.scrollToPosition(viewAdapter.itemCount - 1)
            calcShitBehavior.state = CalcSheetBehavior.State.COLLAPSED }








        viewAdapter = CalcAdapter(CalcFacade)
        calcDecorator = CalcItemDecorator(this)


        calcShitBehavior = CalcSheetBehavior<View>().from(calc_sheet)



        calcShitBehavior.addOnSlideListener { _, relativeSheetPosition ->
            linearLayout.foreground.alpha = 255*relativeSheetPosition.toInt()/100
            (recyclerView.getChildAt(recyclerView.childCount - 1) as MotionLayout).progress = relativeSheetPosition/100
        }

        calcShitBehavior.addOnStateChangedListener { state -> recyclerView.editText.isCursorVisible = state == CalcSheetBehavior.State.COLLAPSED}

        lastCalcSheetState = calcShitBehavior.state

        CalcFacade.initCalcSheet(calcShitBehavior)

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

        CalcFacade.onButtonClicked((view as Button).text.toString())
//        Log.i("XYU", "нажали")
////        val test = ThreadExample()
////        test.testThread(viewAdapter)
//        val testThread = Thread(Runnable {
//            Thread.sleep(1000)
//            Log.i("CalcFacade", "new Thread db loaded")
//
//        })
//        testThread.start()



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

//        val animator = ValueAnimator.ofFloat(200.0f, 30.0f)
//        animator.duration = 4000L
//        animator.addUpdateListener { valueAnimator -> recyclerView.editText.textSize = (valueAnimator.animatedValue as Float)
//        Log.i("CALCCALC", "${recyclerView.editText.textSize}")}
//        animator.start()
    }


    //Считалка
    fun onEq (view: View) {
//        try {
//            dispVal.setText(Calculator().calculate(dispVal.text.toString()))
//            dispVal.setSelection(dispVal.text.length)
//        } catch (exception: IncorrectExpressionException) {
//            Toast.makeText(applicationContext, exception.reason, Toast.LENGTH_SHORT).show()
//        }

        CalcFacade.onCalculateClicked()
    }

    fun openHistory(menuItem: MenuItem) {
        calcShitBehavior.state = CalcSheetBehavior.State.EXPANDED
    }

    fun clearHistory(menuItem: MenuItem) {
        CalcFacade.clearHistory()
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

}


