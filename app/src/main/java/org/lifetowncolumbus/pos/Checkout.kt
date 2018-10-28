package org.lifetowncolumbus.pos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import java.text.NumberFormat

class Checkout : AppCompatActivity() {
    var total = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
    }

    fun launchAddItemDialog(view: View) {
        //TODO: use a view model for this
        total += 1.0
        val format = NumberFormat.getCurrencyInstance()
        findViewById<TextView>(R.id.total).apply {
            text = format.format(total)
        }

    }
}
