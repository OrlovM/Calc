package com.calc.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume

class CustomMotionLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :  MotionLayout(context, attrs, defStyleAttr) {

    private lateinit var calcSheetBehavior: CalcSheetBehavior<View>


    private fun onSlide(relativeSheetPosition: Float) {
        progress = relativeSheetPosition / 100
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!this::calcSheetBehavior.isInitialized) {
            try {
                calcSheetBehavior = CalcSheetBehavior<View>().from(this)
                calcSheetBehavior.addOnSlideListener { _, relativeSheetPosition -> onSlide(relativeSheetPosition)}
            } catch (e: Exception) { Log.i("CustomMotionLayout", "Is not a child of CoordinatorLayout")
            }
        }

    }



    /**
     * Wait for the transition to complete so that the given [transitionId] is fully displayed.
     *
     * @param transitionId The transition set to await the completion of
     * @param timeout Timeout for the transition to take place. Defaults to 5 seconds.
     */
    suspend fun awaitTransitionComplete(transitionId: Int, timeout: Long = 5000L) {
        // If we're already at the specified state, return now
        if (currentState == transitionId && progress == 1f) return

        var listener: MotionLayout.TransitionListener? = null

        try {
            withTimeout(timeout) {
                suspendCancellableCoroutine<Unit> { continuation ->
                    val l = object : TransitionAdapter() {
                        override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                            if (currentId == transitionId) {
                                removeTransitionListener(this)
                                continuation.resume(Unit)
                            }
                        }
                    }
                    // If the coroutine is cancelled, remove the listener
                    continuation.invokeOnCancellation {
                        removeTransitionListener(l)
                    }
                    // And finally add the listener
                    addTransitionListener(l)
                    listener = l
                }
            }
        } catch (tex: TimeoutCancellationException) {
            // Transition didn't happen in time. Remove our listener and throw a cancellation
            // exception to let the coroutine know
            listener?.let(::removeTransitionListener)
            throw CancellationException("Transition to state with id: $transitionId did not" +
                    " complete in timeout.", tex)
        }
    }
}