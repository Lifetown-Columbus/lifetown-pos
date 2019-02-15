package org.lifetowncolumbus.pos.merchant.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_pay_cash.view.*
import org.lifetowncolumbus.pos.KeyboardHelpers
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.merchant.viewModels.CashPayment
import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale


class PayCashFragment : androidx.fragment.app.Fragment() {
    private lateinit var currentSale: CurrentSale
    private lateinit var amountTendered: TextView
    private lateinit var acceptCashButton: Button
    private lateinit var cancelButton: Button
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        amountTendered = view.amountTendered
        acceptCashButton =  view.acceptCashButton
        cancelButton = view.cancelPaymentButton
        navController = Navigation.findNavController(view)

        cancelButton.setOnClickListener { navController.popBackStack() }

        initAcceptCashButton(view)
        initAmountTenderedField()
    }

    private fun initAmountTenderedField() {
        amountTendered.addTextChangedListener(ValidAmountTextWatcher())
        amountTendered.setOnEditorActionListener { _, actionId, _ ->
            KeyboardHelpers.clickButtonWhenKeyboardDone(actionId, acceptCashButton)
        }
    }

    inner class ValidAmountTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val amount = amountTendered.text.toString().toDoubleOrNull() ?: 0.0
            acceptCashButton.isEnabled = amount >= currentSale.total.toDouble()
        }

        override fun afterTextChanged(s: Editable?) { }
    }

    private fun initAcceptCashButton(view: View) {
        acceptCashButton.isEnabled = false
        acceptCashButton.setOnClickListener {
            val amount = amountTendered.text.toString().toDoubleOrNull() ?: 0.0
            currentSale.payCash(CashPayment.worth(amount))
            navController.navigate(R.id.saleCompleteFragment)
            context?.let { context -> KeyboardHelpers.closeKeyboard(context, view) }
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
        currentSale = activity?.run {
            ViewModelProviders.of(this).get(CurrentSale::class.java)
        } ?: throw Exception("Invalid Activity")
    }
}
