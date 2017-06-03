package net.huannguyen.guillotinemenu

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import net.huannguyen.guillotinemenu.ui.HomeController

class MainActivity : AppCompatActivity() {

    private lateinit var mRouter: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val container = findViewById(R.id.container) as ViewGroup
        mRouter = Conductor.attachRouter(this, container, savedInstanceState)

        if (!mRouter.hasRootController()) {
            mRouter.setRoot(RouterTransaction.with(HomeController()))
        }
    }

    override fun onBackPressed() {
        if (!mRouter.handleBack()) {
            super.onBackPressed()
        }
    }
}
