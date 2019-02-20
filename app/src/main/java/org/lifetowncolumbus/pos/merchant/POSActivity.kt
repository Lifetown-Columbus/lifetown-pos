package org.lifetowncolumbus.pos.merchant

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.epson.epos2.discovery.Discovery
import com.epson.epos2.discovery.FilterOption
import com.epson.epos2.printer.Printer
import org.lifetowncolumbus.pos.R

class POSActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    lateinit var printer: Printer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pos)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        configureToolbar(host)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        configurePrinter()
    }

    override fun onDestroy() {
        super.onDestroy()
        Discovery.stop()
    }

    private fun configurePrinter() {
        val filterOption = FilterOption().apply {
            portType = Discovery.PORTTYPE_USB
            deviceModel = Discovery.MODEL_ALL
            deviceType = Discovery.TYPE_PRINTER
        }
        Discovery.start(this, filterOption) {
            printer = Printer(it.deviceType, Printer.MODEL_ANK, this)
            printer.connect(it.target, Printer.PARAM_DEFAULT)
        }
    }

    private fun configureToolbar(host: NavHostFragment) {
        navController = host.navController
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        NavigationUI.setupWithNavController(toolbar, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.config_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,navController) || super.onOptionsItemSelected(item)
    }
}
