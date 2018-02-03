package net.huannguyen.guillotinemenu.ui.misc

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Interpolator
import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler
import net.huannguyen.guillotinemenu.ui.HomeView

private const val GUILLOTINE_CLOSED_ANGLE = 90f
private const val GUILLOTINE_OPENED_ANGLE = 0f

// Feel free to modify these values (of course they need to be meaningful) to try different animation effects.
private const val ACCELERATE_DURATION = 290L
private const val BOUNCE_DURATION = 335L
private const val FIRST_BOUNCE_RATIO = 0.625f
private const val BOUNCE_ANGLE = 3f

class MenuOpenChangeHandler : AnimatorChangeHandler() {

    override fun getAnimator(container: ViewGroup, from: View?, to: View?, isPush: Boolean, toAddedToContainer: Boolean): Animator {
        if (from == null || to !is MenuView) {
            throw IllegalArgumentException("The 'from' view must be non null. The 'to' view must be MenuView.")
        }

        val menuOpenAnimator = ObjectAnimator.ofFloat(to, View.ROTATION, GUILLOTINE_CLOSED_ANGLE, GUILLOTINE_OPENED_ANGLE).apply {
            duration = ACCELERATE_DURATION
            interpolator = AccelerateInterpolator()
            onStart { to.onMenuOpenAnimStart() }
            onEnd { to.playMenuOpenIconAnim() }
        }

        val menuBounceAnimator = ObjectAnimator.ofFloat(to, View.ROTATION, GUILLOTINE_OPENED_ANGLE, -BOUNCE_ANGLE).apply {
            duration = BOUNCE_DURATION
            interpolator = MenuBounceInterpolator()
        }

        return AnimatorSet().apply {
            playSequentially(menuOpenAnimator, menuBounceAnimator)
        }
    }

    override fun resetFromView(from: View) {}
}

class MenuCloseChangeHandler : AnimatorChangeHandler() {

    override fun getAnimator(container: ViewGroup, from: View?, to: View?, isPush: Boolean, toAddedToContainer: Boolean): Animator {
        if (from !is HomeView || to !is FeatureView) {
            throw IllegalArgumentException("The 'from' view must be a HomeView. The 'to' view must be a FeatureView")
        }

        val menuFadeAnimator = ObjectAnimator.ofFloat(from.menuGroup, View.ALPHA, 1f, 0f)

        val menuCloseAnimator = ObjectAnimator.ofFloat(from, View.ROTATION, GUILLOTINE_OPENED_ANGLE, GUILLOTINE_CLOSED_ANGLE).apply {
            interpolator = MenuAccelerateInterpolator()
            onEnd {
                from.alpha = 0f
                to.playMenuCloseIconAnim()
            }
        }

        val toolbarAnimator = ObjectAnimator.ofFloat(to.toolbar, View.ROTATION, GUILLOTINE_OPENED_ANGLE, BOUNCE_ANGLE).apply {
            duration = BOUNCE_DURATION
            interpolator = MenuBounceInterpolator()
        }

        return AnimatorSet().apply {
            playSequentially(AnimatorSet().apply {
                playTogether(menuFadeAnimator, menuCloseAnimator)
                duration = ACCELERATE_DURATION
            }, toolbarAnimator)
        }
    }

    override fun resetFromView(from: View) {
        from.alpha = 1f
    }
}

class MenuAccelerateInterpolator : Interpolator {
    override fun getInterpolation(input: Float) = Math.pow(input.toDouble(), 2.0).toFloat()
}

class MenuBounceInterpolator : Interpolator {

    override fun getInterpolation(input: Float): Float {
        if (input < FIRST_BOUNCE_RATIO) return firstBounce(input)
        return secondBounce(input)
    }

    private fun firstBounce(input: Float): Float {
        return (-4f * BOUNCE_ANGLE / Math.pow(FIRST_BOUNCE_RATIO.toDouble(), 2.0).toFloat()) * Math.pow(input.toDouble(), 2.0).toFloat() + (4f * BOUNCE_ANGLE / FIRST_BOUNCE_RATIO) * input
    }

    private fun secondBounce(input: Float): Float {
        return ((-2 * BOUNCE_ANGLE / (Math.pow(1.0 - FIRST_BOUNCE_RATIO.toDouble(), 2.0))) * Math.pow(input.toDouble(), 2.0)).toFloat() +
                (2 * BOUNCE_ANGLE * (FIRST_BOUNCE_RATIO + 1f) / (Math.pow(1.0 - FIRST_BOUNCE_RATIO.toDouble(), 2.0)) * input).toFloat() +
                (-2 * BOUNCE_ANGLE * FIRST_BOUNCE_RATIO / (Math.pow(1.0 - FIRST_BOUNCE_RATIO.toDouble(), 2.0))).toFloat()
    }
}

interface MenuView {
    fun playMenuOpenIconAnim()
    fun onMenuOpenAnimStart()
}

interface FeatureView {
    val toolbar: View
    fun playMenuCloseIconAnim()
}

inline private fun Animator.onStart(crossinline onStart: () -> Unit) {
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) = onStart()
        override fun onAnimationEnd(animation: Animator?) {}
        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationRepeat(animation: Animator?) {}
    })
}

inline private fun Animator.onEnd(crossinline onEnd: () -> Unit) {
    addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {}
        override fun onAnimationEnd(animation: Animator?) = onEnd()
        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationRepeat(animation: Animator?) {}
    })
}
