package com.calc.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.calc.R



class CalcToolBarBehavior @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null) : CoordinatorLayout.Behavior<View>(context, attrs) {

    private val fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
    private val fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out)
    private var isFadingOut = false

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {

        val params = dependency.layoutParams
        require(params is CoordinatorLayout.LayoutParams) { "The view is not a child of CoordinatorLayout" }
        val behavior =
            params.behavior
        return behavior is CalcSheetBehavior<*>
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val calcSheetBehavior = CalcSheetBehavior<View>().from(dependency)


        if (dependency.top == calcSheetBehavior.collapsedOffset && child.visibility == View.INVISIBLE) {
            //Show ToolBar
            fadeInAnimation.setAnimationListener(object: Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {}
                override fun onAnimationStart(animation: Animation) {
                    child.visibility = View.VISIBLE
                }
            })
            child.startAnimation(fadeInAnimation)
            return true

            } else if (dependency.top != calcSheetBehavior.collapsedOffset && child.visibility == View.VISIBLE && !isFadingOut) {
            //Hide ToolBar
            fadeOutAnimation.setAnimationListener(object: Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    child.visibility = View.INVISIBLE
                    isFadingOut = false
                }
                override fun onAnimationStart(animation: Animation) {
                    child.visibility = View.VISIBLE
                    isFadingOut = true
                }
            })
            child.startAnimation(fadeOutAnimation)
            return true

        } else return false
    }

}