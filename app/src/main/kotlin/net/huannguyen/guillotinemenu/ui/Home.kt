package net.huannguyen.guillotinemenu.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.bindView
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.globalLayouts
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import net.huannguyen.guillotinemenu.R
import net.huannguyen.guillotinemenu.ui.misc.MenuCloseChangeHandler
import net.huannguyen.guillotinemenu.ui.misc.MenuOpenChangeHandler
import net.huannguyen.guillotinemenu.ui.misc.MenuView
import net.huannguyen.guillotinemenu.ui.misc.AnimatedToolbar
import net.huannguyen.guillotinemenu.ui.misc.ToolbarMode
import net.huannguyen.guillotinemenu.utils.getNavigationIconView
import net.huannguyen.guillotinemenu.utils.plusAssign

class HomeController : Controller() {

    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.home, container, false)
    }

    override fun onAttach(view: View) {
        fun pushController(controller: Controller) {
            router.pushController(RouterTransaction.with(controller)
                    .pushChangeHandler(MenuCloseChangeHandler())
                    .popChangeHandler(MenuOpenChangeHandler()))
        }

        disposables += (view as HomeView).musicClicks.subscribe {
            pushController(MusicController())
        }

        disposables += (view as HomeView).videoClicks.subscribe {
            pushController(VideoController())
        }
    }

    override fun onDetach(view: View) {
        disposables.clear()
    }
}

class HomeView(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet), MenuView {
    val menuGroup by bindView<ViewGroup>(R.id.menu_group)
    private val toolbar by bindView<AnimatedToolbar>(R.id.toolbar)
    private val musicMenuItem by bindView<TextView>(R.id.music)
    private val videoMenuItem by bindView<TextView>(R.id.video)

    val musicClicks: Observable<Unit>
        get() = musicMenuItem.clicks()

    val videoClicks: Observable<Unit>
        get() = videoMenuItem.clicks()

    override fun onFinishInflate() {
        super.onFinishInflate()
        toolbar.setMode(ToolbarMode.MODE_BURGER)
        globalLayouts().take(1).subscribe {
            val toolbarIconView = toolbar.getNavigationIconView()
            pivotX = (toolbarIconView.left + toolbarIconView.width/2).toFloat()
            pivotY = (toolbarIconView.top + toolbarIconView.height/2).toFloat()
        }
    }

    override fun playMenuOpenIconAnim() {
        toolbar.setMode(ToolbarMode.MODE_ARROW_TO_BURGER)
    }

    override fun onMenuOpenAnimStart() {
        toolbar.setMode(ToolbarMode.MODE_UP_ARROW)
    }
}