package com.calc.ui


import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import com.example.calc.R
import java.lang.ref.WeakReference


class CalcSheetBehavior<V: View> @JvmOverloads constructor(
    context: Context? = null, attrs: AttributeSet? = null) : CoordinatorLayout.Behavior<View>(context, attrs) {



    abstract interface OnSlideListener {
        abstract fun onSlide(dy: Int)
    }




    abstract class CalcSheetCallback {
        /**
         * Called when the calc sheet changes its state.
         *
         * @param calcSheet The bottom sheet view.
         * @param newState The new state. This will be one of [.STATE_DRAGGING], [     ][.STATE_SETTLING], [.STATE_EXPANDED], [.STATE_COLLAPSED], [     ][.STATE_HIDDEN], or [.STATE_HALF_EXPANDED].
         */
        abstract fun onStateChanged(
            calcSheet: View,
            newState: State
        )


        /**
         * Called when the bottom sheet is being dragged.
         *
         * @param calcSheet The bottom sheet view.
         * @param slideOffset The new offset of this bottom sheet within [-1,1] range. Offset increases
         * as this bottom sheet is moving upward. From 0 to 1 the sheet is between collapsed and
         * expanded states and from -1 to 0 it is between hidden and collapsed states.
         */
        open fun onSlide(calcSheet: View, slideOffset: Int, relativeDy: Int) {}
    }


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
    private var callback = ArrayList<CalcSheetCallback>()
    private var onSlideListeners = ArrayList<(dy: Int, slideOffset: Int) -> Unit>()
    private var onStateChangedListeners = ArrayList<(newState: State) -> Unit>()
    var previousState = State.COLLAPSED
    var relativeSheetPosition = 0.0f
        private set

    fun addOnSlideListener(handler: (dy: Int, slideOffset: Int) -> Unit) {
        onSlideListeners.add(handler)
    }

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
                    settleToState(viewRef.get()!!, value)

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


    fun setOnSlideListener(listener: OnSlideListener) {

    }

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
        // The ViewDragHelper tries to capture only the top-most View. We have to explicitly tell it
        // to capture the bottom sheet in case it is not captured and the touch slop is passed.

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
        return behavior as CalcSheetBehavior<V>
    }


    fun setCallback(callback: CalcSheetCallback) {
        this.callback.add(callback)

    }

    private val dragCallback = object: ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            val canScroll = nestedScrollingChildRef.get()?.canScrollVertically(1)

            if (state == State.EXPANDED && touchingScrollingChild && canScroll!!) return false
//            if (state == State.EXPANDED && touchingScrollingChild) return false


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
            relativeSheetPosition = ((expandedOffset.toFloat() - collapsedOffset.toFloat()) +  top.toFloat()) / (expandedOffset.toFloat() - collapsedOffset.toFloat()) * 100
            var relativeDy = (dy+1)/(Math.abs(dy)+1)*((expandedOffset.toFloat() - collapsedOffset.toFloat()) +  Math.abs(dy)) / (expandedOffset.toFloat() - collapsedOffset.toFloat())

            callback.forEach{it.onSlide(viewRef.get()!!, dy, relativeDy.toInt())}
            onSlideListeners.forEach{it.invoke(dy, 10)}
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            var settleFromViewDragHelper: Boolean
            var finalTop: Int
            var state: State
            if (Math.abs(yvel) > maximumVelocity) {
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
            // STATE_SETTLING won't animate the material shape, so do that here with the target state.

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
            callback.forEach{it.onStateChanged(viewRef.get()!!, state)}
            onStateChangedListeners.forEach{it.invoke(state)}
        }

        if (viewRef == null) {
            return
        }

    }

//    private fun settleToStatePendingLayout(state: State) {
//        val child = viewRef!!.get() ?: return
//        // Start the animation; wait until a pending layout if there is one.
//        val parent = child.parent
//        if (parent != null && parent.isLayoutRequested && ViewCompat.isAttachedToWindow(child)) {
//            val finalState = state
//            child.post { settleToState(child, finalState) }
//        } else {
//            settleToState(child, state)
//        }
//    }



    private fun settleToState(child: View, state: State) {
        var top: Int = when (state) {
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

    @VisibleForTesting
    private fun findScrollingChild(view: View?): View? {
        if (ViewCompat.isNestedScrollingEnabled(view!!)) {
            return view
        }
        if (view is ViewGroup) {
            val group = view
            var i = 0
            val count = group.childCount
            while (i < count) {
                val scrollingChild = findScrollingChild(group.getChildAt(i))
                if (scrollingChild != null) {
                    return scrollingChild
                }
                i++
            }
        }
        return null
    }

    }
