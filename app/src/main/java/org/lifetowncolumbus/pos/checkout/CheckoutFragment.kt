package org.lifetowncolumbus.pos.checkout

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.Navigation
import org.lifetowncolumbus.pos.R
import java.math.BigDecimal
import java.text.NumberFormat


class CheckoutFragment : Fragment() {
    lateinit var checkout: Checkout
    lateinit var itemValue: EditText
    lateinit var totalValue: TextView
    lateinit var addItemButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemValue = view.findViewById(R.id.itemValue)
        totalValue = view.findViewById(R.id.total)
        addItemButton = view.findViewById(R.id.addItemButton)
        val payCashButton: Button = view.findViewById(R.id.payCashButton)

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
        payCashButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.payCashFragment))
        renderTotal()
    }

    private fun addItem(view: View) {
        val amount = itemValue.text.toString().toDoubleOrNull()
        if (amount != null) checkout.addItem(Item(BigDecimal.valueOf(amount)))

        itemValue.apply { text = null }

        renderTotal()
        closeKeyboard(view)
    }

    private fun closeKeyboard(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun renderTotal() {
        totalValue.apply {
            text = NumberFormat.getCurrencyInstance().format(checkout.total)
        }
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
