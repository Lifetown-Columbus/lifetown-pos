package org.lifetowncolumbus.pos.merchant.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.lifetowncolumbus.pos.KeyboardHelpers
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.databinding.FragmentPayCashBinding
import org.lifetowncolumbus.pos.merchant.viewModels.CashPayment
import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale


class PayCashFragment : androidx.fragment.app.Fragment() {
    private var _binding: FragmentPayCashBinding? = null

    private val binding get() = _binding!!
    private val amountTendered: TextView get() = binding.amountTendered
    private val acceptCashButton: Button get() = binding.acceptCashButton
    private val cancelButton: Button get() = binding.cancelPaymentButton

    private lateinit var currentSale: CurrentSale
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            navController.navigate(R.id.action_payCashFragment_to_printReceiptFragment)
            context?.let { context -> KeyboardHelpers.closeKeyboard(context, view) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPayCashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentSale = activity?.run {
            ViewModelProvider(this).get(CurrentSale::class.java)
        } ?: throw Exception("Invalid Activity")
    }
}
