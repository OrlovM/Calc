package com.calc.ui

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import kotlin.properties.Delegates


class CalcLayoutManager(context: Context, private val calcSheet: CalcSheetBehavior<View>? = null): RecyclerView.LayoutManager() {

    lateinit var recyclerVal: RecyclerView.Recycler
    private var TAG = "CalcLayoutManager"
    var scrollTargetPosition: Int? = null
    var scrollTargetBottom: Int? = null
    var scrollUp = false

    private val calcSheetCallback = object: CalcSheetBehavior.CalcSheetCallback(){

        override fun onStateChanged(CalcSheet: View, state: CalcSheetBehavior.State) {

        }

        override fun onSlide(CalcSheet: View, slideOffset: Int, relativeDy: Int) {

            requestLayout()

        }
    }

    var scrollOffset = 0

    init {
        calcSheet?.setCallback(calcSheetCallback)
    }


    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State?) {
        Log.i(TAG, "onLayoutChildren")
        var anchorPosition = scrollTargetPosition ?: findLastVisibleView()?.let { getPosition(it) }
        var anchorBottom = scrollTargetBottom ?: findLastVisibleView()?.let { getDecoratedBottom(it) }
        detachAndScrapAttachedViews(recycler)
        if (scrollUp) fillDown(recycler, anchorPosition, anchorBottom)
        else fillUp(recycler, anchorPosition, anchorBottom)
        fillDown(recycler)
        scrollTargetPosition = null
        scrollTargetBottom = null
        scrollUp = false
        recyclerVal = recycler




    }

    override fun canScrollVertically(): Boolean {
        return calcSheet?.state == CalcSheetBehavior.State.EXPANDED
    }

    override fun scrollVerticallyBy(dy: Int, recycler: Recycler, state: RecyclerView.State?): Int {

        val delta = computeVerticalScrollDistance(dy)

        offsetChildrenVertical(-delta)
        scrollOffset += delta
        fillDown(recycler)
        fillUp(recycler)
        recycleViewOutOfBounds(recycler)


        return delta
    }

    override fun scrollToPosition(targetPosition: Int) {
        if (targetPosition >= getPosition(findFirstVisibleView()!!) && targetPosition <= getPosition(findLastVisibleView()!!)) {
            Log.i(TAG, "видно")
            var delta = when {
                targetPosition == getPosition(findFirstVisibleView()!!) -> 0 - getDecoratedTop(findFirstVisibleView()!!)
                targetPosition == getPosition(findLastVisibleView()!!) -> height - getDecoratedBottom(findLastVisibleView()!!)
                else -> 0
            }
            Log.i(TAG, "$delta")
            offsetChildrenVertical(delta)
        } else {
            scrollTargetPosition = targetPosition
            var Up = targetPosition < getPosition(findFirstVisibleView()!!)
            if (Up) {
                scrollUp = true
                scrollTargetBottom = 0
            } else {
                scrollTargetBottom = height
            }
            requestLayout()
//            scrollToPosition(targetPosition)


        }

    }

    override fun onAdapterChanged(
        oldAdapter: RecyclerView.Adapter<*>?,
        newAdapter: RecyclerView.Adapter<*>?
    ) {
        super.onAdapterChanged(oldAdapter, newAdapter)
    }

    private fun computeVerticalScrollDistance(dy: Int): Int {

        if (childCount == 0) {
            Log.i(TAG, "childCount == 0")
            return 0
        }
        val topView = getChildAt(0)
        val bottomView = getChildAt(childCount - 1)

        //Случай, когда все вьюшки поместились на экране
        val viewSpan = getDecoratedBottom(bottomView!!) - getDecoratedTop(topView!!)
        if (viewSpan <= height) {
            Log.i(TAG, "viewSpan <= height")
            return 0

        }
        var delta = 0
        //если контент уезжает вниз
        if (dy < 0) {
            val firstView: View? = getChildAt(0)
            val firstViewAdapterPos = getPosition(firstView!!)

            delta = if (firstViewAdapterPos > 0) { //если верхняя вюшка не самая первая в адаптере
                dy
            } else { //если верхняя вьюшка самая первая в адаптере и выше вьюшек больше быть не может
                val viewTop = getDecoratedTop(firstView)

                Math.max(viewTop, dy)
            }
        } else if (dy > 0) { //если контент уезжает вверх
            val lastView: View? = getChildAt(childCount - 1)
            val lastViewAdapterPos = getPosition(lastView!!)
            delta =
                if (lastViewAdapterPos < itemCount-1) { //если нижняя вюшка не самая последняя в адаптере
                    dy
                } else { //если нижняя вьюшка самая последняя в адаптере и ниже вьюшек больше быть не может
                    val viewBottom = getDecoratedBottom(lastView)
                    val parentBottom = height
                    Math.min(viewBottom - parentBottom, dy)

                }
        }
        return delta
    }

    private fun fillDown(recycler: Recycler, anchorPosition: Int? = null, anchorViewTop: Int? = null) {

        var pos = anchorPosition ?: getChildAt(childCount-1)?.let { getPosition(it) + 1} ?: childCount
        var viewTop = anchorViewTop ?: getChildAt(childCount-1)?.let { getDecoratedBottom(it) } ?: 0
        var isEmptySpaceAtBottom = viewTop <= height

        while (isEmptySpaceAtBottom && pos < itemCount) {
            val view = recycler.getViewForPosition(pos)
            addView(view)
            measureChildWithMargins(view, 0, 0)
            view.layoutParams.height = 300
            measureChildWithMargins(view, 0, 0)
            layoutDecorated(
                view,
                0,
                viewTop,
                getDecoratedMeasuredWidth(view),
                viewTop + getDecoratedMeasuredHeight(view)
            )
            viewTop += getDecoratedMeasuredHeight(view)
            isEmptySpaceAtBottom = viewTop <= height
            Log.i(TAG, "fillDown pos $pos top $viewTop ${getDecoratedMeasuredHeight(view)}")

            pos += 1
        }
    }

//    private fun fillUp (recycler: Recycler) {
//        var pos = getChildAt(0)?.let { getPosition(it) - 1} ?: itemCount-1
//        var viewTop = getChildAt(0)?.top ?: height
//        fillUpByPosition(pos, viewTop, recycler)
//    }

    private fun fillUp(recycler: Recycler, anchorPosition: Int? = null, anchorViewBottom: Int? = null) {


        var pos = anchorPosition ?: getChildAt(0)?.let { getPosition(it) - 1} ?: itemCount-1
        var viewBottom = anchorViewBottom ?: getChildAt(0)?.let { getDecoratedTop(it) } ?: height
        var isEmptySpaceAtTop = viewBottom >= 0
        while (isEmptySpaceAtTop && pos >= 0) {
            val view = recycler.getViewForPosition(pos)
            addView(view, 0)
            measureChildWithMargins(view, 0, 0)
            view.layoutParams.height = 300
            if (pos == itemCount - 1 ) view.layoutParams.height = 400 - 100*calcSheet?.relativeSheetPosition!!.toInt()/100
            measureChildWithMargins(view, 0, 0)

            layoutDecorated(
                view,
                0,
                viewBottom - getDecoratedMeasuredHeight(view),
                getDecoratedMeasuredWidth(view),
                viewBottom
            )
            Log.i(TAG, "fillUp pos $pos top $viewBottom ${getDecoratedMeasuredHeight(view)}")
            viewBottom -= getDecoratedMeasuredHeight(view)
            isEmptySpaceAtTop = viewBottom >= 0

            pos -= 1
        }
    }

    private fun recycleViewOutOfBounds(recycler: Recycler) {
        for (i in (childCount - 1) downTo 0) {
            val child = getChildAt(i)
            if (child != null && (getDecoratedBottom(child) < 0 || getDecoratedTop(child) > height)) {
                detachView(child)
                recycler.recycleView(child)
                Log.i(TAG, "Child at $i recycled, adapter position ${getPosition(child)}")
            }

        }

    }

    private fun View.setHeightByPosition(position: Int) {
        var lp = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
//        this.layoutParams = lp
        if (calcSheet?.state == CalcSheetBehavior.State.COLLAPSED && position == 0) {
            this.layoutParams.height = this.layoutParams.height*2
        }

    }




//    override fun computeVerticalScrollOffset(state: RecyclerView.State): Int {
//        if (childCount == 0) {
//            return 0
//        }
//        return scrollOffset
//    }
//
//    override fun computeVerticalScrollExtent(state: RecyclerView.State): Int {
//        if (childCount == 0) {
//            return 0
//        }
//        return height
//    }
//
//    override fun computeVerticalScrollRange(state: RecyclerView.State): Int {
//        if (childCount == 0) {
//            return 0
//        }
//        return (itemCount-1)*450
//    }

    val SMOOTH_VALUE = 100
    override fun computeVerticalScrollExtent(state: RecyclerView.State): Int {
        val count = childCount
        return if (count > 0) {
            SMOOTH_VALUE * 3
        } else 0
    }

    //Computes the vertical size of all the content (scrollbar track)
    override fun computeVerticalScrollRange(state: RecyclerView.State): Int {
        return Math.max((itemCount - 1) * SMOOTH_VALUE, 0)
    }

    //Computes the vertical distance from the top of the screen (scrollbar position)
    override fun computeVerticalScrollOffset(state: RecyclerView.State): Int {
        val count = childCount
        if (count <= 0) {
            return 0
        }
        if (findLastCompletelyVisibleItemPosition() == itemCount - 1) {
            return Math.max((itemCount - 1) * SMOOTH_VALUE, 0)
        }
        val heightOfScreen: Int
        val firstPos: Int = findFirstVisibleItemPosition()
        if (firstPos == RecyclerView.NO_POSITION) {
            return 0
        }
        val view = findViewByPosition(firstPos) ?: return 0
        // Top of the view in pixels
        val top = getDecoratedBottom(view)
        val height = getDecoratedMeasuredHeight(view)
        if (height <= 0) {
            heightOfScreen = 0
        } else {
            heightOfScreen = Math.abs(SMOOTH_VALUE * top / height)
        }
        return if (heightOfScreen == 0 && firstPos > 0) {
            SMOOTH_VALUE * firstPos - 1
        } else SMOOTH_VALUE * firstPos + heightOfScreen
    }

    private fun findFirstVisibleItemPosition(): Int {
        return getChildAt(0)?.let { getPosition(it) } ?: RecyclerView.NO_POSITION
    }

    private fun findLastCompletelyVisibleItemPosition(): Int {
        return when (getDecoratedBottom(getChildAt(childCount - 1)!!) == height) {
            true -> getPosition(getChildAt(childCount - 1)!!)
            false -> getPosition(getChildAt(childCount - 2)!!)
        }
    }

    private fun findLastVisibleView(): View? {
        if (childCount == 0) return null
        return when (getChildAt(childCount - 1)?.top!! < height) {
            true -> getChildAt(childCount - 1)!!
            false -> getChildAt(childCount - 2)!!
        }
    }

    private fun findFirstVisibleView(): View? {
        if (childCount == 0) return null
        return when (getChildAt(0)?.bottom!! > 0) {
            true -> getChildAt(0)!!
            false -> getChildAt(0 + 1)!!
        }
    }




}