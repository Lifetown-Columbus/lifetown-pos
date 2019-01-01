package org.lifetowncolumbus.pos.merchant

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_pay_cash.view.*
import org.lifetowncolumbus.pos.KeyboardHelpers
import org.lifetowncolumbus.pos.R


class PayCashFragment : Fragment() {
    private lateinit var checkout: Checkout
    private lateinit var amountTendered: TextView
    private lateinit var acceptCashButton: Button
    private lateinit var cancelButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        amountTendered = view.amountTendered
        acceptCashButton =  view.acceptCashButton
        cancelButton = view.cancelPaymentButton

        cancelButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.checkoutFragment))

        initAcceptCashButton(view)
        initAmountTenderedField()
    }

    private fun initAmountTenderedField() {
        amountTendered.addTextChangedListener(ValidAmountTextWatcher())
        amountTendered.setOnEditorActionListener { v, actionId, event ->
            KeyboardHelpers.clickButtonWhenKeyboardDone(actionId, acceptCashButton)
        }
    }

    inner class ValidAmountTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val amount = amountTendered.text.toString().toDoubleOrNull() ?: 0.0
            acceptCashButton.isEnabled = amount >= checkout.total.toDouble()
        }

        override fun afterTextChanged(s: Editable?) { }
    }

    private fun initAcceptCashButton(view: View) {
        acceptCashButton.isEnabled = false
        acceptCashButton.setOnClickListener {
            val amount = amountTendered.text.toString().toDoubleOrNull() ?: 0.0
            checkout.payCash(CashPayment.worth(amount))
            Navigation.findNavController(view).navigate(R.id.saleCompleteFragment)
            KeyboardHelpers.closeKeyboard(context!!, view)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pay_cash, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkout = activity?.run {
            ViewModelProviders.of(this).get(Checkout::class.java)
        } ?: throw Exception("Invalid Activity")
    }
}
