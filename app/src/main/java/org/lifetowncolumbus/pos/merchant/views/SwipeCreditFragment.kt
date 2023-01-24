package org.lifetowncolumbus.pos.merchant.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.databinding.FragmentSwipeDebitBinding
import org.lifetowncolumbus.pos.magneticCards.SwipeEventHandler
import org.lifetowncolumbus.pos.merchant.POSActivity
import org.lifetowncolumbus.pos.merchant.models.AccountTransactionResult
import org.lifetowncolumbus.pos.merchant.viewModels.CreditPayment
import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale
import org.lifetowncolumbus.pos.services.BankService

class SwipeCreditFragment : Fragment() {
    private var _binding: FragmentSwipeDebitBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentSale: CurrentSale
    private lateinit var navController: NavController
    private lateinit var bankService: BankService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancelSwipeButton.setOnClickListener { Navigation.findNavController(view).popBackStack() }
        navController = Navigation.findNavController(view)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSwipeDebitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as POSActivity).swipeEventHandler = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bankService = BankService()
        activity?.run {
            currentSale = ViewModelProvider(this)[CurrentSale::class.java]

            (activity as POSActivity).swipeEventHandler = SwipeEventHandler { bankCard ->
                Log.e("Card", "Card swiped: ${bankCard.accountNumber}")
                bankService.chargeCard(bankCard.accountNumber, currentSale.total.toDouble()) {
                    if(it == AccountTransactionResult.SUCCESS) {
                        currentSale.payCredit(CreditPayment.worth(currentSale.total.toDouble()))
                        navController.navigate(R.id.action_swipeCreditFragment_to_printReceiptFragment)
                    } else if(it == AccountTransactionResult.DECLINED) {
                        activity?.runOnUiThread {
                            findViewById<TextView>(R.id.swipeCardMessage).text = getString(R.string.CardDeclinedMessage)
                        }
                    } else {
                        activity?.runOnUiThread {
                            findViewById<TextView>(R.id.swipeCardMessage).text =
                                getString(R.string.CardErrorMessage)
                        }
                    }
                }
            }
        } ?: throw Exception("Invalid Activity")
    }
}
