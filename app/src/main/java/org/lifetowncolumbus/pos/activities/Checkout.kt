package org.lifetowncolumbus.pos.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.lifetowncolumbus.pos.R
import java.text.NumberFormat

class Checkout : AppCompatActivity() {
    var total = 0.0
    lateinit var itemValue: EditText
    lateinit var totalValue: TextView
    lateinit var addItemButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        itemValue = findViewById(R.id.itemValue)
        totalValue = findViewById(R.id.total)
        addItemButton = findViewById(R.id.addItemButton)

        itemValue.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    addItemButton.performClick()
                    true
                }
                else -> false
            }
        }

        addToTotal(0.0)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }


    fun addItem(view: View) {
        val amount = itemValue.text.toString().toDoubleOrNull()
        addToTotal(amount ?: 0.0)
        itemValue.apply {
            text = null
        }
    }

    private fun addToTotal(value: Double) {
        total += value

        val format = NumberFormat.getCurrencyInstance()
        totalValue.apply {
            text = format.format(total)
        }
    }
}
