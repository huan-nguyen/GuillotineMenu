package net.huannguyen.guillotinemenu.ui.misc

import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import com.jakewharton.rxbinding2.view.globalLayouts
import net.huannguyen.guillotinemenu.R
import net.huannguyen.guillotinemenu.utils.getDrawable
import net.huannguyen.guillotinemenu.utils.getNavigationIconView
import kotlin.LazyThreadSafetyMode.NONE

class AnimatedToolbar(context: Context, attributeSet: AttributeSet): Toolbar(context, attributeSet) {

    private val burgerToArrowDrawable: Drawable by lazy(NONE) { getDrawable(R.drawable.ic_burger_to_arrow) }
    private val arrowToBurgerDrawable: Drawable by lazy(NONE) { getDrawable(R.drawable.ic_arrow_to_burger) }
    private val burgerDrawable: Drawable by lazy(NONE) { getDrawable(R.drawable.ic_burger) }
    private val backArrowDrawable: Drawable by lazy(NONE) { getDrawable(R.drawable.ic_arrow_back) }
    private val upArrowDrawable: Drawable by lazy(NONE) { getDrawable(R.drawable.ic_arrow_up) }

    fun setMode(mode: ToolbarMode) {
        when(mode) {
            ToolbarMode.MODE_BURGER -> {
                navigationIcon = burgerDrawable
            }
            ToolbarMode.MODE_BACK_ARROW -> {
                navigationIcon = backArrowDrawable
            }
            ToolbarMode.MODE_UP_ARROW -> {
                navigationIcon = upArrowDrawable
            }
            ToolbarMode.MODE_BURGER_TO_ARROW -> {
                navigationIcon = burgerToArrowDrawable
                (burgerToArrowDrawable as Animatable).start()
            }
            ToolbarMode.MODE_ARROW_TO_BURGER -> {
                navigationIcon = arrowToBurgerDrawable
                (arrowToBurgerDrawable as Animatable).start()
            }
        }
    }

    fun setupPivot() {
        globalLayouts().take(1).subscribe {
            val iconView = getNavigationIconView()
            pivotX = (iconView.left + iconView.width / 2).toFloat()
            pivotY = (iconView.top + iconView.height / 2).toFloat()
        }
    }
}

enum class ToolbarMode {
    MODE_BURGER,
    MODE_BACK_ARROW,
    MODE_UP_ARROW,
    MODE_ARROW_TO_BURGER,
    MODE_BURGER_TO_ARROW
}