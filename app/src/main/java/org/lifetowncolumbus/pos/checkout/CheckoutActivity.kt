package org.lifetowncolumbus.pos.checkout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import androidx.navigation.fragment.NavHostFragment
import org.lifetowncolumbus.pos.R

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }
}
