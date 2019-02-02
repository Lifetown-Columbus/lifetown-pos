package org.lifetowncolumbus.pos.merchant

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import androidx.navigation.fragment.NavHostFragment
import org.lifetowncolumbus.pos.R

class POSActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pos)

        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }
}
