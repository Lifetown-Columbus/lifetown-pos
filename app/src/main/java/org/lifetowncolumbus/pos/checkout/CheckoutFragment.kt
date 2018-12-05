package org.lifetowncolumbus.pos.checkout

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.lifetowncolumbus.pos.R
import java.text.NumberFormat


class CheckoutFragment : Fragment() {
    var total = 0.0
    lateinit var itemValue: EditText
    lateinit var totalValue: TextView
    lateinit var addItemButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemValue = view.findViewById(R.id.itemValue)
        totalValue = view.findViewById(R.id.total)
        addItemButton = view.findViewById(R.id.addItemButton)

        itemValue.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    addItemButton.performClick()
                    true
                }
                else -> false
            }
        }
        addItemButton.setOnClickListener {
            addItem(it)
        }

        addToTotal(0.0)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance() = CheckoutFragment()
    }
}
