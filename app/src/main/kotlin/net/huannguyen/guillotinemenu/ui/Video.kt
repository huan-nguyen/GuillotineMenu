package net.huannguyen.guillotinemenu.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import butterknife.bindView
import com.bluelinelabs.conductor.Controller
import com.jakewharton.rxbinding2.support.v7.widget.navigationClicks
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import net.huannguyen.guillotinemenu.R
import net.huannguyen.guillotinemenu.ui.misc.FeatureView
import net.huannguyen.guillotinemenu.ui.misc.AnimatedToolbar
import net.huannguyen.guillotinemenu.ui.misc.ToolbarMode

class VideoController : Controller() {

    private var disposable = Disposables.disposed()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.video, container, false)
    }

    override fun onAttach(view: View) {
        disposable = (view as VideoView).backClicks.subscribe { router.popCurrentController() }
    }

    override fun onDetach(view: View) {
        disposable.dispose()
    }
}

class VideoView(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet), FeatureView {

    override val toolbar by bindView<AnimatedToolbar>(R.id.toolbar)

    val backClicks: Observable<Unit>
        get() = toolbar.navigationClicks()

    override fun onFinishInflate() {
        super.onFinishInflate()
        toolbar.setTitle(R.string.video_library_title)
        toolbar.setMode(ToolbarMode.MODE_BACK_ARROW)
        toolbar.setupPivot()
    }

    override fun playMenuCloseIconAnim() {
        toolbar.setMode(ToolbarMode.MODE_BURGER_TO_ARROW)
    }
}