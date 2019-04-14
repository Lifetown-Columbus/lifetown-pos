package org.lifetowncolumbus.pos.merchant.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_swipe_debit.view.*
import org.lifetowncolumbus.pos.R
import org.lifetowncolumbus.pos.magneticCards.SwipeEventHandler
import org.lifetowncolumbus.pos.merchant.POSActivity
import org.lifetowncolumbus.pos.merchant.viewModels.CurrentSale
import org.lifetowncolumbus.pos.merchant.viewModels.CreditPayment
import org.lifetowncolumbus.pos.services.BankService

class SwipeDebitFragment : Fragment() {
    private lateinit var currentSale: CurrentSale
    private lateinit var navController: NavController
    private lateinit var bankService: BankService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.cancelSwipeButton.setOnClickListener { Navigation.findNavController(view).popBackStack() }
        navController = Navigation.findNavController(view)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_swipe_debit, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as POSActivity).swipeEventHandler = null
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bankService = BankService()
        activity?.run {
            currentSale = ViewModelProviders.of(this).get(CurrentSale::class.java)

            (activity as POSActivity).swipeEventHandler = SwipeEventHandler {
                Log.e("Card", "Card swiped: ${it.accountNumber}")
                bankService.withdraw(it.accountNumber, currentSale.total.toDouble(), {
                    // on success
                    currentSale.payCredit(CreditPayment.worth(currentSale.total.toDouble()))
                    navController.navigate(R.id.saleCompleteFragment)
                }, {
                    // on Declined/Failure
                    // should probably include a failure reason.
                    activity?.runOnUiThread {
                        findViewById<TextView>(R.id.swipeCardMessage).text = "Your card was declined!"
                    }
                })
            }
        } ?: throw Exception("Invalid Activity")
    }
}
