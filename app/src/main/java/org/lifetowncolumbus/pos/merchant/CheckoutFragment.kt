package org.lifetowncolumbus.pos.merchant

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import org.lifetowncolumbus.pos.KeyboardHelpers
import org.lifetowncolumbus.pos.R
import java.math.BigDecimal


class CheckoutFragment : Fragment() {
    lateinit var checkout: Checkout
    lateinit var itemValue: EditText
    lateinit var addItemButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemValue = view.findViewById(R.id.itemValue)
        addItemButton = view.findViewById(R.id.addItemButton)
        addItemButton.setOnClickListener { addItem(it) }

        itemValue.setOnEditorActionListener { v, actionId, event ->
            KeyboardHelpers.clickButtonWhenKeyboardDone(actionId, addItemButton)
        }
    }

    private fun addItem(view: View) {
        val amount = itemValue.text.toString().toDoubleOrNull()
        if (amount != null) checkout.addItem(PurchasedItem(BigDecimal.valueOf(amount)))

        itemValue.apply { text = null }
        KeyboardHelpers.closeKeyboard(context!!, view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkout = activity?.run {
            ViewModelProviders.of(this).get(Checkout::class.java)
        } ?: throw Exception("Invalid Activity")
    }
}
