package com.calc.ui


import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import com.example.calc.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.lang.ref.WeakReference
import kotlin.math.abs


class CalcSheetBehavior<V: View> @JvmOverloads constructor(
    context: Context? = null, attrs: AttributeSet? = null) : CoordinatorLayout.Behavior<View>(context, attrs) {






    private var maximumVelocity: Float = 0.0f
    private lateinit var nestedScrollingChildRef: (WeakReference<View?>)
    private lateinit var viewRef: WeakReference<View>
    private var touchingScrollingChild = false
    private lateinit var dragHelper: ViewDragHelper
    var peekHeight = 200
    var collapsedOffset = 0
    var expandedOffset = 0
    private var halfExpandedOffset = 0
    private var internalState = State.COLLAPSED

    private var onSlideListeners = ArrayList<(dy: Int, relativeSheetPosition: Float) -> Unit>()
    private var onStateChangedListeners = ArrayList<(newState: State) -> Unit>()
    var previousState = State.COLLAPSED
    var relativeSheetPosition = 0.0f
        private set




    /**
     * Adds the lambda function that will be called when the sheet is being dragged.
     *
     * @param dy The distance of drag
     * @param relativeSheetPosition Current position in percents of sheet path distance
     */
    fun addOnSlideListener(handler: (dy: Int, relativeSheetPosition: Float) -> Unit) {
        onSlideListeners.add(handler)
    }

    /**
     *  Adds the lambda function that will be called when the calc sheet changes its state.
     *
     * @param newState The new state. This will be one of [.STATE_DRAGGING], [.STATE_SETTLING], [.STATE_EXPANDED], [.STATE_COLLAPSED].
     */
    fun addOnStateChangedListener(handler: (newState: State) -> Unit) {
        onStateChangedListeners.add(handler)
    }


    var state: State
        get() = internalState
        set(value) {
            if (internalState != value) {
                if (!this::viewRef.isInitialized) {
                    // The view is not laid out yet; modify internalState and let onLayoutChild handle it later
                    if (value == State.COLLAPSED || value == State.EXPANDED)
                    {
                        internalState = value
                    }
                }else{
                    viewRef.get()?.let { settleToState(it, value) }

            }
        }
        }
    private var parentHeight = 0


    init {

        if (context != null) {
            val configuration = ViewConfiguration.get(context)
            maximumVelocity = configuration.scaledMinimumFlingVelocity.toFloat()*2
            val attributes =
                context.obtainStyledAttributes(attrs, R.styleable.CalcSheetBehavior)
            peekHeight = attributes.getDimensionPixelSize(R.styleable.CalcSheetBehavior_peekHeight, 600)
            attributes.recycle()
        }
    }

    enum class State {COLLAPSED, EXPANDED, DRAGGING, SETTLING}



    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        if (ViewCompat.getFitsSystemWindows(parent) && !ViewCompat.getFitsSystemWindows(child)) {
            child.fitsSystemWindows = true
        }
        if (!this::viewRef.isInitialized) {
        viewRef = WeakReference<View>(child)
        }

        nestedScrollingChildRef = WeakReference(findScrollingChild(child))
        if (!this::dragHelper.isInitialized) {
            dragHelper = ViewDragHelper.create(parent, 1.0f, dragCallback)
        }
        val savedTop = child.top
        parent.onLayoutChild(child, layoutDirection)
        parentHeight = parent.height
        collapsedOffset = peekHeight - child.height
        expandedOffset = parentHeight - child.height
        halfExpandedOffset = (collapsedOffset + expandedOffset)/2


        if (internalState == State.EXPANDED) {
            ViewCompat.offsetTopAndBottom(child, expandedOffset)
        } else if (internalState == State.COLLAPSED) {
            ViewCompat.offsetTopAndBottom(child, collapsedOffset)
        } else if (internalState == State.DRAGGING || internalState == State.SETTLING) {
            ViewCompat.offsetTopAndBottom(child, savedTop)
        }
        return true
    }


    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: View, ev: MotionEvent): Boolean {

        if (ev.actionMasked == MotionEvent.ACTION_CANCEL) {
            touchingScrollingChild = false
        }
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            val initialX = ev.x.toInt()
            val initialY = ev.y.toInt()
            val scroll =
                if (this::nestedScrollingChildRef.isInitialized) nestedScrollingChildRef.get() else null
            if (scroll != null && parent.isPointInChildBounds(scroll, initialX, initialY)) {

                touchingScrollingChild = true
            }
        }

        return dragHelper.shouldInterceptTouchEvent(ev)

    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: View, ev: MotionEvent): Boolean {

        if (this::dragHelper.isInitialized){
            dragHelper.processTouchEvent(ev)

        }
        return true

    }


    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {

        return true

    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {

    }



    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        // Overridden to prevent the default consumption of the entire scroll distance.
    }


    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        type: Int
    ) {

    }

    /**
     * A utility function to get the [CalcSheetBehavior] associated with the `view`.
     *
     * @param view The [View] with [CalcSheetBehavior].
     * @return The [CalcSheetBehavior] associated with the `view`.
     */
    fun from(view: V): CalcSheetBehavior<V> {
        val params = view.layoutParams
        require(params is CoordinatorLayout.LayoutParams) { "The view is not a child of CoordinatorLayout" }
        val behavior =
            params.behavior
        require(behavior is CalcSheetBehavior<*>) { "The view is not associated with BottomSheetBehavior" }
        @Suppress("UNCHECKED_CAST")
        return behavior as CalcSheetBehavior<V>
    }




    private val dragCallback = object: ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            val canScroll = nestedScrollingChildRef.get()?.canScrollVertically(1) ?: false

            if (state == State.EXPANDED && touchingScrollingChild && canScroll) return false


            return child == viewRef.get()
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return MathUtils.clamp(top, collapsedOffset, parentHeight-child.height)
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return child.height*2
        }

        override fun onViewDragStateChanged(state: Int) {
            if (state == ViewDragHelper.STATE_DRAGGING) {

                setStateInternal(State.DRAGGING)

            }
        }

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            calculateRelativeSheetPosition(top)
            dispatchListeners(dy)

        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val settleFromViewDragHelper: Boolean
            val finalTop: Int
            val state: State
            if (abs(yvel) > maximumVelocity) {
                finalTop = when {yvel >0 -> expandedOffset
                                 else -> collapsedOffset}
                settleFromViewDragHelper = true
            }else{
                finalTop = if (releasedChild.top > halfExpandedOffset) {
                    expandedOffset
                }else{
                    collapsedOffset
                }
                settleFromViewDragHelper = false

            }
            state = when (finalTop) {
                expandedOffset -> State.EXPANDED
                else -> State.COLLAPSED
            }

            startSettlingAnimation(releasedChild, state, finalTop, settleFromViewDragHelper)

        }

    }

    private fun dispatchListeners(dy: Int) {
        onSlideListeners.forEach{it.invoke(dy, relativeSheetPosition)}
    }

    private fun calculateRelativeSheetPosition(top: Int) {
        relativeSheetPosition = ((expandedOffset.toFloat() - collapsedOffset.toFloat()) +  top.toFloat()) / (expandedOffset.toFloat() - collapsedOffset.toFloat()) * 100
    }


    private fun startSettlingAnimation(
        child: View,
        internalState: State,
        top: Int,
        settleFromViewDragHelper: Boolean
    ) {
        val startedSettling: Boolean =
            if (settleFromViewDragHelper) dragHelper.settleCapturedViewAt(
                child.left,
                top
            ) else dragHelper.smoothSlideViewTo(child, child.left, top)
        if (startedSettling) {

            setStateInternal(State.SETTLING)


            ViewCompat.postOnAnimation(child, object : Runnable{
                override fun run() {

                    if (dragHelper.continueSettling(true)) {
                        ViewCompat.postOnAnimation(child, this)

                    }else {
                        setStateInternal(internalState)
                    }
                }
            })

        } else {
            setStateInternal(internalState)
        }

    }
    private fun setStateInternal(state: State) {
        if (internalState != state) {
            previousState = internalState
            internalState = state
            onStateChangedListeners.forEach{it.invoke(state)}
        }

    }


    private fun settleToState(child: View, state: State) {
        val top: Int = when (state) {
            State.COLLAPSED -> {
                collapsedOffset
            }
            State.EXPANDED -> {
                expandedOffset
            }
            else -> {
                throw IllegalArgumentException("Illegal state argument: $state")
            }
        }
        startSettlingAnimation(child, state, top, false)
    }


    private fun findScrollingChild(view: View): View? {
        if (ViewCompat.isNestedScrollingEnabled(view)) {
            Log.i("NESTEDCHILD", "$view")
            return view
        }
        if (view is ViewGroup) {
            var i = 0
            val count = view.childCount
            while (i < count) {
                val scrollingChild = findScrollingChild(view.getChildAt(i))
                if (scrollingChild != null) {
                    Log.i("NESTEDCHILD", "$view")
                    return scrollingChild
                }
                i++
            }
        }
        Log.i("NESTEDCHILD", "null")
        return null
    }

    }
