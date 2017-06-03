package net.huannguyen.guillotinemenu.utils

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import java.util.*

// referenced from: http://stackoverflow.com/questions/29922995/get-toolbars-navigation-icon-view-referrence
fun Toolbar.getNavigationIconView(): View {
    val hadContentDescription = TextUtils.isEmpty(this.navigationContentDescription)
    val contentDescription = if (!hadContentDescription) this.navigationContentDescription else "navigationIcon"
    this.navigationContentDescription = contentDescription

    val potentialViews = ArrayList<View>()
    //find the view based on it's content description, set programmatically or with android:contentDescription

    this.findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION)

    //Nav icon is always instantiated at this point because calling setNavigationContentDescription ensures its existence
    var navIcon: View? = null
    if(potentialViews.size > 0){
        navIcon = potentialViews[0]
    }

    //Clear content description if not previously present
    if(hadContentDescription) this.navigationContentDescription = null
    return navIcon!!
}

fun View.getDrawable(drawableRes: Int): Drawable = ContextCompat.getDrawable(context, drawableRes)