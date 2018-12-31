package org.lifetowncolumbus.pos.checkout

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.lifetowncolumbus.pos.R
import java.math.BigDecimal
import java.text.NumberFormat


class PayCashFragment : Fragment() {
    private lateinit var changeDue: TextView
    private lateinit var checkout: Checkout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeDue = view.findViewById(R.id.changeDue)

        registerCalculateChangeHandler(view)
    }

    private fun registerCalculateChangeHandler(view: View) {
        val amountTendered: TextView = view.findViewById(R.id.amountTendered)
        view.findViewById<Button>(R.id.calculateChangeButton).setOnClickListener {
            val amount = amountTendered.text.toString().toDoubleOrNull()
            if (amount != null) {
                calculateChange(amount)
            }
        }
    }

    private fun calculateChange(amount: Double) {
        checkout.payCash(CashPayment.worth(amount))

        changeDue.apply {
            text = NumberFormat.getCurrencyInstance().format(-checkout.total)
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
