package org.lifetowncolumbus.pos.merchant

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        amountTendered = view.amountTendered
        acceptCashButton =  view.calculateChangeButton

        initAcceptCashButton(view)

        amountTendered.setOnEditorActionListener { v, actionId, event ->
            KeyboardHelpers.clickButtonWhenKeyboardDone(actionId, acceptCashButton)
        }
    }

    private fun initAcceptCashButton(view: View) {
        acceptCashButton.setOnClickListener {
            val amount = amountTendered.text.toString().toDoubleOrNull()
            if (amount != null) {
                checkout.payCash(CashPayment.worth(amount))
                Navigation.findNavController(view).navigate(R.id.saleCompleteFragment)
            }
            else {
                Navigation.findNavController(view).navigate(R.id.checkoutFragment)
            }
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
